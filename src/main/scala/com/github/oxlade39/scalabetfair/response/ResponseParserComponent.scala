package com.github.oxlade39.scalabetfair.response

import com.betfair.publicapi.types.exchange.v5._
import com.github.oxlade39.scalabetfair.domain._
import com.betfair.publicapi.types.global.v3.GetEventTypesResp
import org.joda.time.DateTime
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.request.Event
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import scala.Some
import com.betfair.publicapi.util.InflatedCompleteMarketPrices.{InflatedCompletePrice, InflatedCompleteRunner}
import com.betfair.publicapi.util.InflatedCompleteMarketPrices
import com.github.oxlade39.scalabetfair.domain.RunnerPrice

/**
 * @author dan
 */
trait ResponseParserComponent {
  def responseParser: ResponseParser

  trait ResponseParser {
    def toEvents(response: GetEventTypesResp): Either[List[Event], RequestError]
    def toMarketDetails(response: GetAllMarketsResp): Either[List[MarketDetail], RequestError]
    def runnersFromMarket(response: GetMarketResp): Either[List[Runner], RequestError]
    def runnerPrice(response: InflatedCompletePrice): RunnerPrice
    def toMarketPrices(response: GetCompleteMarketPricesCompressedResp,
                       marketName: MarketName,
                       runners: List[Runner]): Either[MarketPrices, RequestError]
  }
}

trait RealResponseParserComponent extends ResponseParserComponent {
  import scala.collection.JavaConversions._

  val responseParser = new ResponseParser {

    def toMarketPrices(response: GetCompleteMarketPricesCompressedResp,
                       marketName: MarketName,
                       runners: List[Runner]): Either[MarketPrices, RequestError] = {
      val prices: InflatedCompleteMarketPrices = new InflatedCompleteMarketPrices(response.getCompleteMarketPrices)

      assert(prices.getMarketId.equals (marketName.id),
        "The marketname must match the market in the compressed prices. %s != %s".format(prices.getMarketId, marketName.id))

      val zipped: List[(Runner, InflatedCompleteRunner)] = runners.sortBy(_.selectionId).zip(prices.getRunners.sortBy(_.getSelectionId))
      val runnerDetails: List[RunnerDetail] = zipped.map {
        case (runner: Runner, bfRunner: InflatedCompleteRunner) =>
          assert(runner.selectionId.equals (bfRunner.getSelectionId),
            "selectionIds do not match, " + runner.selectionId + " != " + bfRunner.getSelectionId)

          RunnerDetail(runner,
            bfRunner.getLastPriceMatched,
            bfRunner.getTotalAmountMatched,
            bfRunner.getPrices.map(price => runnerPrice(price)).toList)
      }
      Left(MarketPrices(marketName, prices.getInPlayDelay, runnerDetails))
    }

    def runnersFromMarket(response: GetMarketResp): Either[List[Runner], RequestError] = {
      response.getErrorCode match {
        case GetMarketErrorEnum.OK => {
          val jmarket: Market = response.getMarket
          val jrunnersArray: ArrayOfRunner = jmarket.getRunners
          val jrunners = jrunnersArray.getRunner
          val runnerBuffer = jrunners.map(bfRunner => Runner(bfRunner.getName, bfRunner.getSelectionId))

          Left(runnerBuffer.toList)
        }
        case _ => Right(RequestError("API error: %s:%s".format(response.getErrorCode, response.getMinorErrorCode)))
      }
    }

    def toMarketDetails(response: GetAllMarketsResp) =
      Option(response).flatMap(r => Option(r.getMarketData)).map(parseGetAllMarketsRespString(_)) match {
        case None => Right(RequestError("API error: %s:%s".format(response.getErrorCode, response.getMinorErrorCode)))
        case Some(x) => Left(x)
      }

    def toEvents(response: GetEventTypesResp): Either[List[Event], RequestError] = {
      import scala.collection.JavaConversions._

      val eventTypes = Option(response.getEventTypeItems).flatMap(et => Option(et.getEventType))
      eventTypes match {
        case None => Right(RequestError("API error: %s:%s".format(response.getErrorCode, response.getMinorErrorCode)))
        case Some(events) => Left(events.toList.map(eventType => Event(eventType.getId, Some(eventType.getName))))
      }
    }

    def runnerPrice(response: InflatedCompletePrice): RunnerPrice =
      RunnerPrice(
        response.getPrice,
        response.getBackAmountAvailable,
        response.getLayAmountAvailable
      )
  }

  private[this] def parseGetAllMarketsRespString(responseString: String): List[MarketDetail] = {
    val marketsUnparsed: Array[String] = responseString.split(":")
    marketsUnparsed.toList.filter(!_.isEmpty) map {
      singleMarketUnparsed: String =>
        val marketFields: Array[String] = singleMarketUnparsed.split("~")
        val marketDetail: MarketDetail = new MarketDetail(
          MarketName(marketFields(0).toInt, marketFields(1)),
          marketFields(2),
          marketFields(3),
          new DateTime().withMillis(marketFields(4).toLong),
          marketFields(5).split("\\\\").drop(1).toList.map(_.trim()),
          marketFields(6).split("/").drop(1).toList,
          marketFields(7),
          marketFields(8).toInt,
          marketFields(9),
          new DateTime().withMillis(marketFields(10).toLong),
          marketFields(11).toInt,
          marketFields(12).toInt,
          marketFields(13).toDouble,
          marketFields(14).eq("Y"),
          marketFields(15).eq("Y")
        )
        marketDetail
    }
  }
}


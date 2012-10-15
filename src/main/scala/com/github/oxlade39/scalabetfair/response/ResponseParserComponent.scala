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

/**
 * @author dan
 */
trait ResponseParserComponent {
  def responseParser: ResponseParser

  trait ResponseParser {
    def toEvents(response: GetEventTypesResp): Either[List[Event], RequestError]
    def toMarketDetails(response: GetAllMarketsResp): Either[List[MarketDetail], RequestError]
    def toMarketPrices(response: GetCompleteMarketPricesCompressedResp): Either[MarketPrices, RequestError]
    def runnersFromMarket(response: GetMarketResp): Either[List[Runner], RequestError]
  }
}

trait RealResponseParserComponent extends ResponseParserComponent {
  val responseParser = new ResponseParser {
    def toMarketPrices(response: GetCompleteMarketPricesCompressedResp) = null

    def runnersFromMarket(response: GetMarketResp): Either[List[Runner], RequestError] = {
      import scala.collection.JavaConversions._

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
      Option(response).flatMap(r => Option(r.getMarketData)).map(parseResponseString(_)) match {
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
  }

  private[this] def parseResponseString(responseString: String): List[MarketDetail] = {
    val marketsUnparsed: Array[String] = responseString.split(":")
    marketsUnparsed.toList.filter(!_.isEmpty) map {
      singleMarketUnparsed: String =>
        val marketFields: Array[String] = singleMarketUnparsed.split("~")
        val marketDetail: MarketDetail = new MarketDetail(
          MarketName(marketFields(0).toInt, marketFields(1)),
          marketFields(2),
          marketFields(3),
          new DateTime().withMillis(marketFields(4).toLong),
          marketFields(5).split("\\\\").toList.map(_.trim()),
          marketFields(6).split("/").toList,
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


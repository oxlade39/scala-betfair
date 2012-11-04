package com.github.oxlade39.scalabetfair.response

import org.specs2.mutable.Specification
import com.betfair.publicapi.types.exchange.v5.{Market => BfMarket, Runner => BfRunner, _}
import io.Source
import org.joda.time.{DateTimeZone, DateTime}
import com.betfair.publicapi.types.global.v3.{EventType, ArrayOfEventType, GetEventTypesResp}
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.request.Event
import scala.Right
import scala.Some
import com.github.oxlade39.scalabetfair.domain
import domain.{Runner, MarketName, RunnerDetail}
import java.util.TimeZone

/**
 * @author dan
 */
class ResponseParserSpec extends Specification {
  val londonTimezone = DateTimeZone.forID("Europe/London")

  val underTest = new RealResponseParserComponent {}.responseParser

  "ResponseParser" should {
    "return a request error if GetMarketResp has errors" in {

      val betfairResponse: GetMarketResp = new GetMarketResp()
      betfairResponse.setErrorCode(GetMarketErrorEnum.INVALID_MARKET)
      betfairResponse.setMinorErrorCode("no such market")

      val market = underTest.runnersFromMarket(betfairResponse)

      market mustEqual Right(RequestError("API error: %s:%s".format(GetMarketErrorEnum.INVALID_MARKET.value(), "no such market")))
    }

    "return a list of runners from GetMarketResp" in {

      val betfairResponse: GetMarketResp = new GetMarketResp()
      betfairResponse.setErrorCode(GetMarketErrorEnum.OK)
      val bfMarket = new BfMarket()
      val bfRunners = new ArrayOfRunner()
      bfRunners.getRunner.add(createRunner(1))
      bfRunners.getRunner.add(createRunner(2))
      bfMarket.setRunners(bfRunners)

      betfairResponse.setMarket(bfMarket)

      val market = underTest.runnersFromMarket(betfairResponse)

      market mustEqual Left(List(Runner("runner1", 1), Runner("runner2", 2)))
    }

    "return marketDetails from GetAllMarketsResp" in {
      val betfairResponse = new GetAllMarketsResp()

      betfairResponse.setMarketData(TestExamples.exampleMarketDataString)

      val response = underTest.toMarketDetails(betfairResponse)

      response.isLeft mustEqual true

      val details = response.left.get
      details.size mustEqual 106
      val firstMarketDetails = details.head

      firstMarketDetails.marketName mustEqual MarketName(107031725,"Both teams to Score?")
      firstMarketDetails.status mustEqual "ACTIVE"
      firstMarketDetails.amountMatched mustEqual 428.62
      firstMarketDetails.menuPath mustEqual List("Soccer","English Soccer","Npower League One","Fixtures 16 October","L Orient v Hartlepool")
      firstMarketDetails.betDelay mustEqual "8"
      firstMarketDetails.country mustEqual("GBR")

      val expectedEventDate = new DateTime(londonTimezone).withYear(2012).withMonthOfYear(10).withDayOfMonth(16)
        .withHourOfDay(19).withMinuteOfHour(45)
        .withSecondOfMinute(0).withMillisOfSecond(0)

      firstMarketDetails.eventDate mustEqual expectedEventDate
      firstMarketDetails.eventHierarchy mustEqual List("1", "258597", "1908054", "26905577", "26905578", "107031725")
      firstMarketDetails.exchangeId mustEqual 1
      firstMarketDetails.lastRefresh mustEqual expectedEventDate.withMinuteOfHour(57).withSecondOfMinute(25).withMillisOfSecond(292)
      firstMarketDetails.marketType mustEqual "O"
      firstMarketDetails.numPosWinners mustEqual 1
      firstMarketDetails.numRunners mustEqual 2
      firstMarketDetails.supportsStartingPrice mustEqual false
      firstMarketDetails.turningInPlay mustEqual false
    }

    "return a list of event from a GetEventTypesResp" in {

      val bfResponse = new GetEventTypesResp()
      val eventTypes = new ArrayOfEventType()
      eventTypes.getEventType.add(createEvent(45453, "Some Event"))
      eventTypes.getEventType.add(createEvent(45454, "Some Other Event"))
      bfResponse.setEventTypeItems(eventTypes)

      val response = underTest.toEvents(bfResponse)

      response.isLeft mustEqual true
      val events = response.left.get

      events mustEqual List(Event(45453, Some("Some Event")), Event(45454, Some("Some Other Event")))
    }

    "return MarketPrices from GetCompleteMarketPricesCompressedResp" in {

      val bfResponse = new GetCompleteMarketPricesCompressedResp
      bfResponse.setCompleteMarketPrices(TestExamples.exampleCompressedCompleteMarketPrices)

      val response: Either[domain.MarketPrices, RequestError] = underTest.toMarketPrices(bfResponse,
        MarketName(107119445, "Market Name"),
        List(Runner("runner1", 30246), Runner("runner2", 30247)))

      response.isLeft mustEqual true
      val prices = response.left.get
      prices.market mustEqual MarketName(107119445, "Market Name")
      prices.inPlayDelay mustEqual 0
      prices.runners.size mustEqual 2

      val runnerDetail: RunnerDetail = prices.runners.head
      runnerDetail.runner mustEqual Runner("runner1", 30246)
      runnerDetail.totalAmountMatched mustEqual 0.0
      runnerDetail.lastPriceMatched mustEqual 0
      val bestBack = runnerDetail.bestBacks.head
      bestBack.backAvailable mustEqual 123.86
      bestBack.price mustEqual 1.02
    }
  }


  def createEvent(id: Int, eventName: String) = {
    val eventType = new EventType()
    eventType.setExchangeId(1)
    eventType.setId(id)
    eventType.setName(eventName)
    eventType
  }

  def createRunner(id: Int) = {
    val runner = new BfRunner()
    runner.setName("runner%s".format(id))
    runner.setSelectionId(id)
    runner
  }
}

object TestExamples {
  lazy val exampleMarketDataString = singleLineFromString("exampleMarketDataString.txt")
  lazy val exampleCompressedCompleteMarketPrices = singleLineFromString("compressedCompleteMarketPrices.txt")

  def singleLineFromString(fileName: String): String = {
    val resource = Thread.currentThread().getContextClassLoader.getResourceAsStream(fileName)
    val source = Source.fromInputStream(resource)
    try {
      source.getLines().next()
    } finally source.close()
  }

}

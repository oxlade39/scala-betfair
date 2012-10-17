package com.github.oxlade39.scalabetfair.service

import org.specs2.mutable.Specification
import com.github.oxlade39.scalabetfair.request._
import com.github.oxlade39.scalabetfair.response.{RealResponseParserComponent, ResponseParserComponent}
import org.specs2.mock.Mockito
import com.betfair.publicapi.types.global.v3.{GetEventTypesResp, GetEventTypesReq}
import org.joda.time.DateTime
import com.github.oxlade39.scalabetfair.domain._
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.request.AllMarketsRequest
import com.github.oxlade39.scalabetfair.request.Event
import com.github.oxlade39.scalabetfair.request.DateRange
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import com.github.oxlade39.scalabetfair.domain.MarketName
import com.betfair.publicapi.types.exchange.v5.{GetMarketResp, GetMarketReq, GetCompleteMarketPricesCompressedResp, GetCompleteMarketPricesCompressedReq, GetAllMarketsReq, GetAllMarketsResp}
import com.github.oxlade39.scalabetfair.session.{FSCredentialsProviderComponent, CredentialsProviderComponent, WsdlSessionProviderComponent}

/**
 * @author dan
 */
class RealBetfairMarketServiceSpec extends Specification with Mockito {

  class UnderTest extends RealBetfairMarketServiceComponent
    with RequestFactoryComponent
    with ResponseParserComponent
    with GlobalServiceComponent
    with ExchangeServiceComponent {

    val responseParser = mock[ResponseParser]
    val globalService = mock[com.betfair.publicapi.v3.bfglobalservice.BFGlobalService]
    val exchangeService = mock[com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService]
    val requestFactory = mock[RequestFactory]
  }

  "RealBetfairMarketService" should {
    "fetch active events from the global betfair service using the request factory and response parser" in {
      val underTest = new UnderTest

      val bfRequest: GetEventTypesReq = new GetEventTypesReq
      val bfResponse: GetEventTypesResp = new GetEventTypesResp
      val parsedResponse: Left[List[Event], Nothing] = Left(List(Event(1), Event(2)))

      underTest.requestFactory.activeEvents returns bfRequest
      underTest.globalService.getActiveEventTypes(bfRequest) returns bfResponse
      underTest.responseParser.toEvents(bfResponse) returns parsedResponse

      val events: Either[List[Event], RequestError] = underTest.activeEvents()

      events mustEqual parsedResponse
    }

    "fetch market details from the betfair exchange service using the request factory and response parser" in {
      val underTest = new UnderTest

      val request: AllMarketsRequest = AllMarketsRequest(Event(1), TodayAndTomorrow)
      val bfRequest = new GetAllMarketsReq
      val bfResponse = new GetAllMarketsResp
      // don't like returning mocks from mocks but the MarketDetail is a beast to construct...consider adding to TestExamples
      val parsedResponse: Either[List[MarketDetail], RequestError] = Left(List(mock[MarketDetail], mock[MarketDetail]))

      underTest.requestFactory.allMarkets(request) returns bfRequest
      underTest.exchangeService.getAllMarkets(bfRequest) returns bfResponse
      underTest.responseParser.toMarketDetails(bfResponse) returns parsedResponse

      val response: Either[List[MarketDetail], RequestError] =
        underTest.allMarkets(request)

      response mustEqual parsedResponse
    }

    "fetch market prices from the betfair exchange service using the request factory and response parser" in {
      val underTest = new UnderTest

      val request = MarketName(34, "Market Name")
      val marketRunners: List[Runner] = List(Runner("Runner1", 1), Runner("Runner2", 2))
      val bfRequest = new GetCompleteMarketPricesCompressedReq
      val bfResponse = new GetCompleteMarketPricesCompressedResp

      val bfMarketRequest = new GetMarketReq
      val bfMarketResponse = new GetMarketResp

      val parsedResponse: Either[MarketPrices, RequestError] =
        Left(MarketPrices(request, 10, List()))

      underTest.requestFactory.market(request.id) returns bfMarketRequest
      underTest.exchangeService.getMarket(bfMarketRequest) returns bfMarketResponse
      underTest.responseParser.runnersFromMarket(bfMarketResponse) returns Left(marketRunners)

      underTest.requestFactory.marketPrices(request) returns bfRequest
      underTest.exchangeService.getCompleteMarketPricesCompressed(bfRequest) returns bfResponse
      underTest.responseParser.toMarketPrices(bfResponse, request, marketRunners) returns parsedResponse

      val response: Either[MarketPrices, RequestError] =
        underTest.marketPrices(request)

      response mustEqual parsedResponse
    }
  }
}

object TodayAndTomorrow extends DateRange(new DateTime(), new DateTime().plusDays(1))

object Example extends App {

  object ExampleService
    extends RealBetfairMarketServiceComponent
    with WsdlRequestFactoryComponent
    with RealResponseParserComponent
    with WsdlGlobalServiceComponent
    with WsdlExchangeServiceComponent
    with HeadersComponent
    with WsdlSessionProviderComponent
    with FSCredentialsProviderComponent {
  }

  val pricesOrError = ExampleService.activeEvents() match {
    case Right(error) => Right(error)
    case Left(activeEvents) => {
      val soccer: Option[Event] = activeEvents.find(e => e.name.isDefined && e.name.get.contains("Soccer"))
      val markets = soccer.map(e => ExampleService.allMarkets(AllMarketsRequest(e, TodayAndTomorrow)))
      markets match {
        case None => Right(RequestError("no market details"))
        case Some(Left(Nil)) => Right(RequestError("no market details"))
        case Some(Right(error)) => Right(error)
        case Some(Left(marketDetails)) => ExampleService.marketPrices(marketDetails.head.marketName)
      }
    }
  }

  println(pricesOrError)



}
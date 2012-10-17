package com.github.oxlade39.scalabetfair.service

import org.specs2.mutable.Specification
import com.github.oxlade39.scalabetfair.request._
import com.github.oxlade39.scalabetfair.response.ResponseParserComponent
import org.specs2.mock.Mockito
import com.betfair.publicapi.types.global.v3.{GetEventTypesResp, GetEventTypesReq}
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.request.AllMarketsRequest
import com.github.oxlade39.scalabetfair.request.Event
import org.joda.time.DateTime
import com.github.oxlade39.scalabetfair.domain.{MarketPrices, MarketName, MarketDetail}
import com.betfair.publicapi.types.exchange.v5.{GetCompleteMarketPricesCompressedResp, GetCompleteMarketPricesCompressedReq, GetAllMarketsResp, GetAllMarketsReq}

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
      val bfRequest = new GetCompleteMarketPricesCompressedReq
      val bfResponse = new GetCompleteMarketPricesCompressedResp
      val parsedResponse: Either[MarketPrices, RequestError] =
        Left(MarketPrices(MarketName(22, "Market Name"), 10, List()))

      underTest.requestFactory.marketPrices(request) returns bfRequest
      underTest.exchangeService.getCompleteMarketPricesCompressed(bfRequest) returns bfResponse
      underTest.responseParser.toMarketPrices(bfResponse) returns parsedResponse

      val response: Either[MarketPrices, RequestError] =
        underTest.marketPrices(request)

      response mustEqual parsedResponse
    }
  }
}

object TodayAndTomorrow extends DateRange(new DateTime(), new DateTime().plusDays(1))
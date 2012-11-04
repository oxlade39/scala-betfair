package com.github.oxlade39.scalabetfair.service

import com.betfair.publicapi.types.exchange.v5._
import com.github.oxlade39.scalabetfair.request._
import com.github.oxlade39.scalabetfair.domain.{Runner, MarketDetail, MarketName, MarketPrices}
import com.betfair.publicapi.types.global.v3.{GetEventTypesReq, GetEventTypesResp}
import com.github.oxlade39.scalabetfair.response.{RealResponseParserComponent, ResponseParserComponent}
import scala.Left
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import com.github.oxlade39.scalabetfair.domain.MarketName
import scala.Left
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import com.github.oxlade39.scalabetfair.domain.MarketName
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.request.AllMarketsRequest
import com.github.oxlade39.scalabetfair.request.Event
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import com.github.oxlade39.scalabetfair.domain.MarketName
import com.github.oxlade39.scalabetfair.session._
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.request.AllMarketsRequest
import com.github.oxlade39.scalabetfair.request.Event
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import com.github.oxlade39.scalabetfair.domain.MarketName
import com.github.oxlade39.scalabetfair.date.Dates
import org.joda.time.DateTime
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.request.AllMarketsRequest
import com.github.oxlade39.scalabetfair.request.Event
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import com.github.oxlade39.scalabetfair.domain.MarketName
import scala.Left
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.MarketPrices
import com.github.oxlade39.scalabetfair.request.AllMarketsRequest
import com.github.oxlade39.scalabetfair.request.Event
import com.github.oxlade39.scalabetfair.domain.Runner
import com.github.oxlade39.scalabetfair.domain.MarketDetail
import scala.Right
import com.github.oxlade39.scalabetfair.domain.MarketName

/**
 * BetfairMarketService delegating to a GlobalServiceComponent and ExchangeServiceComponent
 * for making the actualy requests and the RequestFactoryComponent and ResponseParserComponent
 * for building and parsing the betfair request/response objects.
 * @author dan
 */
trait RealBetfairMarketServiceComponent extends BetfairMarketService {
  self: RequestFactoryComponent
    with ResponseParserComponent
    with GlobalServiceComponent
    with ExchangeServiceComponent =>

  def activeEvents(): Either[List[Event], RequestError] = {
    val bfRequest: GetEventTypesReq = requestFactory.activeEvents
    val response: GetEventTypesResp = globalService.getActiveEventTypes(bfRequest)
    responseParser.toEvents(response)
  }

  def allMarkets(userRequest: AllMarketsRequest): Either[List[MarketDetail], RequestError] = {
    val bfRequest: GetAllMarketsReq = requestFactory.allMarkets(userRequest)
    val response: GetAllMarketsResp = exchangeService.getAllMarkets(bfRequest)

    responseParser.toMarketDetails(response)
  }

  def marketPrices(market: MarketName): Either[MarketPrices, RequestError] = {
    val bfRequest: GetCompleteMarketPricesCompressedReq = requestFactory.marketPrices(market)
    val response: GetCompleteMarketPricesCompressedResp = exchangeService.getCompleteMarketPricesCompressed(bfRequest)

    val bfMarketRequest: GetMarketReq = requestFactory.market(market.id)
    val getMarketResponse = exchangeService.getMarket(bfMarketRequest)
    val maybeRunners: Either[List[Runner], RequestError] = responseParser.runnersFromMarket(getMarketResponse)

    maybeRunners match {
      case Right(error) => Right(error)
      case Left(runners) => responseParser.toMarketPrices(response, market, runners)
    }
  }
}

/**
 * Complete stack of caked modules allowing interaction with the
 * real Betfair Market service using the given Credentials to log in.
 *
 * N.B the credentials will be cached as specified by CachedSessionProviderComponent
 * @param credentials
 */
class CachedSessionMarketService(val credentials: Credentials)
  extends RealBetfairMarketServiceComponent
  with WsdlRequestFactoryComponent
  with RealResponseParserComponent
  with WsdlGlobalServiceComponent
  with WsdlExchangeServiceComponent
  with HeadersComponent
  with CachedSessionProviderComponent
  with CredentialsComponent
  with Dates {

  val directSessionProvider = new WsdlSessionProviderComponent
                                    with WsdlGlobalServiceComponent
                                    with CredentialsComponent {
    def credentials = CachedSessionMarketService.this.credentials
  }

  def delegate = () => directSessionProvider.sessionProvider.sessionToken
  def dateFactory = () => new DateTime()
}


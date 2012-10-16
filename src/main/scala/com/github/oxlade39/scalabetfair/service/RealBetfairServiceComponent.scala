package com.github.oxlade39.scalabetfair.service

import com.betfair.publicapi.types.exchange.v5.{GetAllMarketsReq, GetAllMarketsResp, GetCompleteMarketPricesCompressedReq, GetCompleteMarketPricesCompressedResp}
import com.github.oxlade39.scalabetfair.request.{RequestFactoryComponent, Event, AllMarketsRequest, RequestError}
import com.github.oxlade39.scalabetfair.domain.{MarketDetail, MarketName, MarketPrices}
import com.betfair.publicapi.types.global.v3.{GetEventTypesReq, GetEventTypesResp}
import com.github.oxlade39.scalabetfair.response.ResponseParserComponent

/**
 * @author dan
 */
trait RealBetfairServiceComponent extends BetfairService {
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
    responseParser.toMarketPrices(response)
  }
}


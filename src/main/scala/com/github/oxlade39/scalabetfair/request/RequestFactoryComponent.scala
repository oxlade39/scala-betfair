package com.github.oxlade39.scalabetfair.request

import com.betfair.publicapi.types.global.v3.GetEventTypesReq
import com.betfair.publicapi.types.exchange.v5.{GetAllMarketsReq, GetCompleteMarketPricesCompressedReq, GetMarketReq}
import com.github.oxlade39.scalabetfair.domain.MarketName

/**
 * @author dan
 */
trait RequestFactoryComponent {
  def requestFactory: RequestFactory

  trait RequestFactory {
    def activeEvents: GetEventTypesReq
    def allMarkets(request: AllMarketsRequest): GetAllMarketsReq
    def marketPrices(market: MarketName): GetCompleteMarketPricesCompressedReq
    def market(id: Int): GetMarketReq
  }

}

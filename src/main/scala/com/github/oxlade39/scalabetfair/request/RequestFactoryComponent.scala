package com.github.oxlade39.scalabetfair.request

import com.betfair.publicapi.types.global.v3.GetEventTypesReq
import com.betfair.publicapi.types.exchange.v5._
import com.github.oxlade39.scalabetfair.session.SessionProviderComponent
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

class WsdlRequestFactoryComponent extends RequestFactoryComponent {
  self: HeadersComponent with SessionProviderComponent =>

  import com.github.oxlade39.scalabetfair.date.dateTimeToXMLGregorianCalendar

  def requestFactory = new RequestFactory {

    def activeEvents: GetEventTypesReq = {
      val req: GetEventTypesReq = new GetEventTypesReq()
      req.setHeader(headers.v3Header)
      req
    }

    def allMarkets(request: AllMarketsRequest): GetAllMarketsReq = {

      val allMarketsReq: GetAllMarketsReq = new GetAllMarketsReq()

      val eventIds: ArrayOfInt = new ArrayOfInt()
      eventIds.getInt.add(request.event.id)
      allMarketsReq.setEventTypeIds(eventIds)

      val countryCode: ArrayOfCountryCode = new ArrayOfCountryCode()
      countryCode.getCountry.add("GBR")
      allMarketsReq.setCountries(countryCode)

      allMarketsReq.setFromDate(request.between.from.toLocalDate)
      allMarketsReq.setToDate(request.between.to.toLocalDate)

      allMarketsReq.setHeader(headers.v5header)

      allMarketsReq
    }

    def marketPrices(market: MarketName) = {
      val request: GetCompleteMarketPricesCompressedReq = new GetCompleteMarketPricesCompressedReq()
      request.setCurrencyCode("GBP")
      request.setMarketId(market.id)
      request.setHeader(headers.v5header)
      request
    }

    def market(id: Int) = {
      val request = new GetMarketReq()
      request.setMarketId(id)
      request.setLocale("en")
      request.setHeader(headers.v5header)
      request
    }

  }
}
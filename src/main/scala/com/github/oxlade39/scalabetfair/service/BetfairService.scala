package com.github.oxlade39.scalabetfair.service

import com.github.oxlade39.scalabetfair.request.{AllMarketsRequest, RequestError, Event}
import com.github.oxlade39.scalabetfair.domain.{MarketPrices, MarketName, MarketDetail}

/**
 * The main Betfair service facade which interacts with the remote exchange
 *
 * @author dan
 */
trait BetfairService {

  /**
   * List all Events that are active on the Betfair exchange
   * @return all Events that are active on the Betfair exchange or an error
   */
  def activeEvents(): Either[List[Event], RequestError]

  /**
   * List all MarketDetails on the Betfair exchange
   * which match the given AllMarketsRequest criteria.
   *
   * @param request criteria to match
   * @return all MarketDetails on the Betfair exchange
   * which match the given AllMarketsRequest criteria.
   */

  def allMarkets(request: AllMarketsRequest): Either[List[MarketDetail], RequestError]

  /**
   * List all MarketPrices currently on the Betfair exchange for the given MarketName.
   * @param market the MarketName to search for.
   * @return all MarketPrices currently on the Betfair exchange for the given MarketName.
   */
  def marketPrices(market: MarketName): Either[MarketPrices, RequestError]
}

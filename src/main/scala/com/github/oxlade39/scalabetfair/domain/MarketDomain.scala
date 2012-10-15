package com.github.oxlade39.scalabetfair.domain

/**
 * Represents the identity of a Market on the betfair exchange.
 * For example Man UTD vs Arsenal/Match Odds
 *
 * @param id the unique id of the market
 * @param name the descriptive name of the market
 */
case class MarketName(id: Int, name: String)

/**
 * Represents one of the possible outcomes of a Market on the betfair exchange.
 * For example if the market is Man UTD vs Arsenal/Match Odds then the Runners
 * would be:
 * <ul>
 *  <li>Man UTD</li>
 *  <li>Arsenal</li>
 *  <li>Draw</li>
 * </ul>
 */
case class Runner(name: String, selectionId: Int)

/**
 * A price point on the betfair exchange for a Runner.
 *
 * @param price the price on the exchange
 * @param backAvailable the amount available on the exchange to back at this price
 * @param layAvailable the amount available on the exchange to back at this price
 */
case class RunnerPrice(
  price: BigDecimal,
  backAvailable: BigDecimal,
  layAvailable: BigDecimal
) {
  /**
   * is there any money on the exchange to back at this price
   */
  lazy val isBackable: Boolean = backAvailable > BigDecimal(0)

  /**
   * is there any money on the exchange to lay at this price
   */
  lazy val isLayable: Boolean = layAvailable > BigDecimal(0)
}

/**
 * All the prices on the exchange for a given Runner.
 *
 * @param runner the Runner on the exchange
 * @param lastPriceMatched the last price that watch matched against this runner
 * @param totalAmountMatched the total amount that has been matched against this runner on the exchange
 * @param prices all the RunnerPrices on the exchange for this Runner
 */
case class RunnerDetail(
 runner: Runner,
 lastPriceMatched: BigDecimal,
 totalAmountMatched: BigDecimal,
 prices: List[RunnerPrice]
) {

  /**
   * List of RunnerPrice sorted by highest price first.
   * Filtering out any that have no amount to back available on the exchange
   */
  lazy val bestBacks = prices.filter(_.isBackable).sortBy(_.price * -1)

  /**
   * List of RunnerPrice sorted by lowest price first.
   * Filtering out any that have no amount to lay available on the exchange
   */
  lazy val bestLays = prices.filter(_.isLayable).sortBy(_.price)

}

/**
 * A List of RunnerDetail for an entire Market on the exchange.
 *
 * @param market
 * @param inPlayDelay
 * @param runners
 */
case class MarketPrices(
 market: MarketName,
 inPlayDelay: Int,
 runners: List[RunnerDetail]
)
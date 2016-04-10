package uk.co.softsquare.privetng.response

import org.joda.time.DateTime
import uk.co.softsquare.privetng.util.ReflectiveToString

case class ListMarketBookResponse(
  marketId: String,
  status: String,
  betDelay: Int, //The number of seconds an order is held until it is submitted into the market. Orders are usually delayed when the market is in-play
  bspReconciled: Boolean, // True if the market starting price has been reconciled
  complete: Boolean, //If false, runners may be added to the market
  inplay: Boolean, //True if the market is currently in play
  numberOfWinners: Int,// The number of selections that could be settled as winners
  numberOfRunners: Int,// The number of runners in the market
  numberOfActiveRunners: Int, //The number of runners that are currently active. An active runner is a selection available for betting
  lastMatchTime: Option[DateTime], // The most recent time an order was executed
  totalMatched:  Double, // The total amount matched
  totalAvailable:  Double, // The total amount of orders that remain unmatched
  crossMatching: Boolean,// True if cross matching is enabled for this market.
  runnersVoidable: Boolean,// True if runners in the market can be voided
  version: Long, // The version of the market. The version increments whenever the market status changes, for example, turning in-play, or suspended when a goal score.
  isMarketDataDelayed: Boolean,
  runners: List[Runner]
) extends ReflectiveToString

case class Runner(
  selectionId: Long, // The unique id of the runner (selection)
  handicap: Double, // The handicap.  Enter the specific handicap value (returned by RUNNER in listMaketBook) if the market is an Asian handicap market.
  status: String, // The status of the selection (i.e., ACTIVE, REMOVED, WINNER, LOSER, HIDDEN) Runner status information is available for 90 days following market settlement.
  adjustmentFactor: Option[Double], // The adjustment factor applied if the selection is removed
  lastPriceTraded: Option[Double], // The price of the most recent bet matched on this selection
  totalMatched: Option[Double], // The total amount matched on this runner
  removalDate: Option[DateTime], // If date and time the runner was removed
  sp: Option[StartingPrices], //The BSP related prices for this runner
  ex: Option[ExchangePrices], //The Exchange prices available for this runner
  orders: Option[List[Order]], //List of orders in the market
  matches: Option[List[Match]] //List of matches (i.e, orders that have been fully or partially executed)
) extends ReflectiveToString

case class StartingPrices(
  nearPrice: Option[Double],
  farPrice: Option[Double],
  backStakeTaken: Option[List[PriceSize]],
  layLiabilityTaken: Option[List[PriceSize]],
  actualSP: Option[Double]
) extends ReflectiveToString

case class ExchangePrices(
  availableToBack: Option[List[PriceSize]],
  availableToLay: Option[List[PriceSize]],
  tradedVolume: Option[List[PriceSize]]
) extends ReflectiveToString

case class PriceSize(
  price: Double,
  size: Double
) extends ReflectiveToString

case class Order(
  betId: String,
  orderType: String, //OrderType
  status: String, // -> OrderStatus
  persistenceType: String, // -> PersistenceType
  side: String, // -> Side
  price: Double,
  size: Double,
  bspLiability: Double,
  placedDate: DateTime,
  avgPriceMatched: Option[Double],
  sizeMatched: Option[Double],
  sizeRemaining: Option[Double],
  sizeLapsed: Option[Double],
  sizeCancelled: Option[Double],
  sizeVoided: Option[Double]
) extends ReflectiveToString

case class Match(
  betId: Option[String],
  matchId: Option[String],
  side: String,
  price: Double,
  size: Double,
  matchDate: Option[DateTime]
) extends ReflectiveToString

object RunnerStatus {
  val Active = "ACTIVE"
  val Removed = "REMOVED"
  val Winner = "WINNER"
  val Loser = "LOSER"
  val Hidden = "HIDDEN"
}

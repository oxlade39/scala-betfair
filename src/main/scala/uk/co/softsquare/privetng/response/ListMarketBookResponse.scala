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
  removalDate: Option[DateTime] // If date and time the runner was removed
) extends ReflectiveToString
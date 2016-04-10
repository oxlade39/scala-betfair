package uk.co.softsquare.privetng.request

case class ListMarketBookRequest(
  marketIds: Set[String],
  priceProjection: Option[PriceProjection] = None,
  orderProjection: Option[String] = None, // -> OrderProjection
  matchProjection: Option[String] = None) // -> MatchProjection

case class PriceProjection(
  priceData: Option[Set[String]] = None, // -> PriceData
  exBestOffersOverrides: Option[ExBestOffersOverrides] = None,
  virtualise: Option[String] = None,
  rolloverStakes: Option[String] = None)

case class ExBestOffersOverrides(
  bestPricesDepth: Option[Int]= None,
  rollupModel: Option[String] = None, // -> RollupModel
  rollupLimit: Option[Int] = None,
  rollupLiabilityThreshold: Option[Double] = None,
  rollupLiabilityFactor: Option[Int] = None)

object RollupModel {
  val Stake = "STAKE"
  val Payout = "PAYOUT"
  val ManagedLiability = "MANAGED_LIABILITY"
  val None = "NONE"
}

object PriceData {
  val SpAvailable = "SP_AVAILABLE"
  val SpTraded = "SP_TRADED"
  val ExBestOffers = "EX_BEST_OFFERS"
  val ExAllOffers = "EX_ALL_OFFERS"
  val ExTraded = "EX_TRADED"
}

object OrderProjection {
  val All = "ALL"
  val Executable = "EXECUTABLE"
  val ExecutionComplete = "EXECUTION_COMPLETE"
}

object MatchProjection {
  val NoRollup = "NO_ROLLUP"
  val RolledUpByPrice = "ROLLED_UP_BY_PRICE"
  val RolledUpByAvgPrice = "ROLLED_UP_BY_AVG_PRICE"
}

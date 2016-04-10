package uk.co.softsquare.privetng.enums

object OrderType {
  val Limit = "LIMIT"
  val LimitOnClose = "LIMIT_ON_CLOSE"
  val MarketOnClose = "MARKET_ON_CLOSE"
}

object Side {
  val Back = "BACK"
  val Lay = "LAY"
}

object PersistenceType {
  val Lapse = "LAPSE" // Lapse the order when the market is turned in-play
  val Persist = "PERSIST" // Persist the order to in-play
  val MarketOnClose = "MARKET_ON_CLOSE" // Put the order into the auction (SP) at turn-in-play
}

object OrderStatus {
  val ExecutionComplete = "EXECUTION_COMPLETE"
  val Executable = "EXECUTABLE"
}

object InstructionReportStatus {
  val Success = "SUCCESS"
  val Failure = "FAILURE"
  val Timeout = "TIMEOUT"
}

object InstructionReportErrorCode {
  val InvalidBetSize = "INVALID_BET_SIZE"
  val InvalidRunner = "INVALID_RUNNER"
  val BetTakenOrLapsed = "BET_TAKEN_OR_LAPSED"
  val BetInProgress = "BET_IN_PROGRESS"
  val RunnerRemoved = "RUNNER_REMOVED"
  val MarketNotOpenForBetting = "MARKET_NOT_OPEN_FOR_BETTING"
  val LossLimitExceeded = "LOSS_LIMIT_EXCEEDED"
  val MarketNotOpenForBspBetting = "MARKET_NOT_OPEN_FOR_BSP_BETTING"
  val InvalidPriceEdit = "INVALID_PRICE_EDIT"
  val InvalidOdds = "INVALID_ODDS"
  val InsufficentFunds = "INSUFFICIENT_FUNDS"
  val InvalidPersistenceType = "INVALID_PERSISTENCE_TYPE"
  val ErrorInMatcher = "ERROR_IN_MATCHER"
  val InvalidBackLayCombination = "INVALID_BACK_LAY_COMBINATION"
  val ErrorInOrder = "ERROR_IN_ORDER"
  val InvalidBidType = "INVALID_BID_TYPE"
  val InvalidBetId = "INVALID_BET_ID"
  val CancelledNotPlaced = "CANCELLED_NOT_PLACED"
  val RelatedActionFailed = "RELATED_ACTION_FAILED"
  val NoActionRequired = "NO_ACTION_REQUIRED"
}

object ExecutionReportStatus {
  val Success = "SUCCESS"
  val Failure = "FAILURE"
  val ProcessedWithErrors = "PROCESSED_WITH_ERRORS"
  val Timeout = "TIMEOUT"
}

object ExecutionReportErrorCode {
  val ErrorInMatcher = "ERROR_IN_MATCHER"
  val ProcessedWithErrors = "PROCESSED_WITH_ERRORS"
  val BetActionError = "BET_ACTION_ERROR"
  val InvalidAccountState = "INVALID_ACCOUNT_STATE"
  val InvalidWalletStatus = "INVALID_WALLET_STATUS"
  val InsufficentFunds = "INSUFFICIENT_FUNDS"
  val LossLimitExceeded = "LOSS_LIMIT_EXCEEDED"
  val MarketSuspended = "MARKET_SUSPENDED"
  val MarketNotOpenForBetting = "MARKET_NOT_OPEN_FOR_BETTING"
  val DuplicateTransaction = "DUPLICATE_TRANSACTION"
  val InvalidOrder = "INVALID_ORDER"
  val InvalidMarketId = "INVALID_MARKET_ID"
  val PermissionDenied = "PERMISSION_DENIED"
  val DuplicateBetids = "DUPLICATE_BETIDS"
  val NoActionRequired = "NO_ACTION_REQUIRED"
  val ServiceUnavailable = "SERVICE_UNAVAILABLE"
  val RejectedByRegulator = "REJECTED_BY_REGULATOR"
  val NoChasing = "NO_CHASING"
}

object Wallet {
  val Uk = "UK"
  val Australian = "AUSTRALIAN"
}

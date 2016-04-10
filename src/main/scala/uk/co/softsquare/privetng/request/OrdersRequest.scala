package uk.co.softsquare.privetng.request

import uk.co.softsquare.privetng.util.ReflectiveToString


case class PlaceOrdersRequest(marketId: String,
                            instructions: List[PlaceInstruction],
                            customerRef: Option[String] = None) extends ReflectiveToString

case class CancelOrdersRequest(marketId: String,
                            instructions: List[CancelInstruction],
                            customerRef: Option[String] = None) extends ReflectiveToString

case class ReplaceOrdersRequest(marketId: String,
                            instructions: List[ReplaceInstruction],
                            customerRef: Option[String] = None) extends ReflectiveToString

case class UpdateOrdersRequest(marketId: String,
                            instructions: List[UpdateInstruction],
                            customerRef: Option[String] = None) extends ReflectiveToString


// Instruction to place a new order
case class PlaceInstruction(orderType: String, // -> OrderType
                            selectionId: Long,
                            handicap: Option[Double] = None,
                            side: String, // -> Side
                            limitOrder: Option[LimitOrder] = None,
                            limitOnCloseOrder: Option[LimitOnCloseOrder] = None,
                            marketOnCloseOrder: Option[MarketOnCloseOrder] = None) extends ReflectiveToString

//Instruction to fully or partially cancel an order (only applies to LIMIT orders)
case class CancelInstruction(betId: String,
                            sizeReduction: Option[Double] = None) extends ReflectiveToString

//Instruction to replace a LIMIT or LIMIT_ON_CLOSE order at a new price
case class ReplaceInstruction(betId: String,
                            newPrice: Double) extends ReflectiveToString

//Instruction to update LIMIT bet's persistence of an order that do not affect exposure
case class UpdateInstruction(betId: String,
                            newPersistenceType: String) // -> PersistenceType


case class LimitOrder(size: Double,
                      price: Double,
                      persistenceType: String) extends ReflectiveToString // -> PersistenceType

case class LimitOnCloseOrder(liability: Double,
                             price: Double) extends ReflectiveToString

case class MarketOnCloseOrder(liability: Double) extends ReflectiveToString

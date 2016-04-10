package uk.co.softsquare.privetng

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{JsString, Json, Writes}

package object request {
  implicit val writesJodaDateTime = Writes[DateTime](dateTime => JsString(dateTime.toString(ISODateTimeFormat.dateTime())))
  implicit val timeRangeFormat = Json.format[TimeRange]
  implicit val marketFilterFormat = Json.format[MarketFilter]
  implicit val authorisedRequestFormat = Json.format[AuthorisedRequest]
  implicit val exBestOffersOverridesFormat = Json.format[ExBestOffersOverrides]
  implicit val priceProjectionFormat = Json.format[PriceProjection]
  implicit val listMarketBookRequestFormat = Json.format[ListMarketBookRequest]

  implicit val limitOrderFormat = Json.format[LimitOrder]
  implicit val limitOnCloseOrderFormat = Json.format[LimitOnCloseOrder]
  implicit val MarketOnCloseOrderFormat = Json.format[MarketOnCloseOrder]

  implicit val placeInstructionFormat = Json.format[PlaceInstruction]
  implicit val cancelInstructionFormat = Json.format[CancelInstruction]
  implicit val replaceInstructionFormat = Json.format[ReplaceInstruction]
  implicit val updateInstructionFormat = Json.format[UpdateInstruction]

  implicit val placeOrdersRequestFormat = Json.format[PlaceOrdersRequest]
  implicit val cancelOrdersRequestFormat = Json.format[CancelOrdersRequest]
  implicit val replaceOrdersRequestFormat = Json.format[ReplaceOrdersRequest]
  implicit val updateOrdersRequestFormat = Json.format[UpdateOrdersRequest]
}

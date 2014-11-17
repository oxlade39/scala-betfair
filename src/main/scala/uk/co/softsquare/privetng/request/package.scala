package uk.co.softsquare.privetng

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{JsString, Json, Writes}

package object request {
  implicit val writesJodaDateTime = Writes[DateTime](dateTime => JsString(dateTime.toString(ISODateTimeFormat.dateTime())))
  implicit val timeRangeFormat = Json.format[TimeRange]
  implicit val marketFilterFormat = Json.format[MarketFilter]
  implicit val authorisedRequestFormat = Json.format[AuthorisedRequest]
}

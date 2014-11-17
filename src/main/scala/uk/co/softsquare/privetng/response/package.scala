package uk.co.softsquare.privetng

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{Json, Reads}

package object response {

  implicit val readsJodaDateTime = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, ISODateTimeFormat.dateTime())
    )
  )
  implicit val eventFormat = Json.format[Event]
  implicit val eventTypeFormat = Json.format[EventType]
  implicit val listEventsResponseFormat = Json.format[ListEventsResponse]
  implicit val listEventTypesResponseFormat = Json.format[ListEventTypesResponse]
  implicit val marketCatalogueFormat = Json.format[MarketCatalogueResponse]
  implicit val runnerFormat = Json.format[Runner]
  implicit val listMarketBookResponseFormat = Json.format[ListMarketBookResponse]
}

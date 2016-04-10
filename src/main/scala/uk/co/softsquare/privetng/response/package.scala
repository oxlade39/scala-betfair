package uk.co.softsquare.privetng

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{Json, Reads}
import uk.co.softsquare.privetng.request.{CancelInstruction, PlaceInstruction, ReplaceInstruction, UpdateInstruction}

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
  implicit val matchFormat = Json.format[Match]
  implicit val orderFormat = Json.format[Order]
  implicit val priceSizeFormat = Json.format[PriceSize]
  implicit val exchangePricesFormat = Json.format[ExchangePrices]
  implicit val startingPricesFormat = Json.format[StartingPrices]
  implicit val runnerFormat = Json.format[Runner]
  implicit val listMarketBookResponseFormat = Json.format[ListMarketBookResponse]
  implicit val competitionFormat = Json.format[Competition]
  implicit val listCompetitionsResponseFormat = Json.format[ListCompetitionsResponse]

  implicit val placeInstructionFormat = Json.format[PlaceInstruction]
  implicit val cancelInstructionFormat = Json.format[CancelInstruction]
  implicit val replaceInstructionFormat = Json.format[ReplaceInstruction]
  implicit val updateInstructionFormat = Json.format[UpdateInstruction]

  implicit val placeInstructionReportFormat = Json.format[PlaceInstructionReport]
  implicit val cancelInstructionReportFormat = Json.format[CancelInstructionReport]
  implicit val replaceInstructionFReportormat = Json.format[ReplaceInstructionReport]
  implicit val updateInstructionReportFormat = Json.format[UpdateInstructionReport]

  implicit val placeExecutionReportFormat = Json.format[PlaceExecutionReport]
  implicit val cancelExecutionReportFormat = Json.format[CancelExecutionReport]
  implicit val replaceExecutionReportFormat = Json.format[ReplaceExecutionReport]
  implicit val updateExecutionReportFormat = Json.format[UpdateExecutionReport]

  implicit val accountFundsResponseFormat = Json.format[AccountFundsResponse]
}

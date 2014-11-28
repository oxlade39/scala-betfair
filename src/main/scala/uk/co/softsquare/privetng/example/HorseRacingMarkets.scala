package uk.co.softsquare.privetng.example

import uk.co.softsquare.privetng.request.{TimeRange, MarketFilter, AuthorisedRequest}
import uk.co.softsquare.privetng.response.{MarketCatalogueResponse, Event}

import scala.concurrent.Future


object HorseRacingMarkets extends ExampleApp {
  import betfair.ex
  
  val HorseRacingEventTypeId = "7"

  val horseRacingMarkets = for {
    session <- betfair.login(MyCredentials)
    events <- betfair.listEvents(AuthorisedRequest(token = session.token, maxResults = 100, filter = MarketFilter(
      eventTypeIds = Set(HorseRacingEventTypeId),
      marketStartTime = Some(TimeRange.Today)
    )))
    selectedEvent <- Future(events.find(response => selectEvent(response.event)))
    markets <- {
      betfair.listMarketCatalogue(AuthorisedRequest(token = session.token, maxResults = 20, filter = MarketFilter(
        eventIds = selectedEvent.map(response => Set(response.event.id)).getOrElse(Set.empty[String])
      )))
    }
  } yield SelectedEvent(selectedEvent.map(_.event), markets)

  horseRacingMarkets onSuccess {
    case SelectedEvent(Some(event), markets) =>
      println(s"Selected Event: ${event.name}")
      markets foreach println
      betfair.http.shutdown()
  }
  
  def selectEvent: Event => Boolean = { event => event.name.toLowerCase.contains("a")}

  case class SelectedEvent(event: Option[Event], markets: List[MarketCatalogueResponse])
}
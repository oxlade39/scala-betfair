package uk.co.softsquare.privetng.example

import uk.co.softsquare.privetng.request.{TimeRange, MarketFilter, AuthorisedRequest}


object TodaysHorseRacingEvents extends ExampleApp {
  import betfair.ex
  
  val HorseRacingEventTypeId = "7"

  val horseRacingEvents = for {
    session <- betfair.login(MyCredentials)
    events <- betfair.listEvents(AuthorisedRequest(token = session.token, maxResults = 100, filter = MarketFilter(
      eventTypeIds = Set(HorseRacingEventTypeId),
      marketStartTime = Some(TimeRange.Today)
    )))
  } yield events

  horseRacingEvents onSuccess {
    case events =>
      events foreach println
      betfair.http.shutdown()
  }
}
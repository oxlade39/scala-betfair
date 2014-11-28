package uk.co.softsquare.privetng.example

import uk.co.softsquare.privetng.request.AuthorisedRequest

object FindEventTypeByName extends App {
  new FindEventTypeByName("horse")
}

class FindEventTypeByName(name: String) extends Example {
  import betfair.ex

  val horseEventTypes = for {
    session <- betfair.login(MyCredentials)
    eventTypes <- betfair.listEventTypes(AuthorisedRequest(token = session.token, maxResults = 100))
  } yield eventTypes.filter(_.eventType.name.toLowerCase.contains(name))

  horseEventTypes onSuccess {
    case events =>
      events foreach println
      betfair.http.shutdown()
  }
}
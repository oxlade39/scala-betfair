package uk.co.softsquare.privetng.service

import uk.co.softsquare.privetng.auth.Credentials
import uk.co.softsquare.privetng.request.{AuthorisedRequest, MarketFilter, TimeRange}

import scala.concurrent.{ExecutionContext, Future}

object MarketsTest extends App {
  val bf = new WSBetfair {
    override def executionContext: ExecutionContext = ExecutionContext.global
  }
  import bf.ex

  val soccer = for {
    loginResponse <- bf.login(Credentials.fromConsole())
    eventTypes <- bf.listEventTypes(AuthorisedRequest(loginResponse.token))
    competitions <- {
      val soccerEvent = eventTypes.find(eventType => eventType.eventType.name.toLowerCase.contains("soccer"))
      bf.listCompetitions(AuthorisedRequest(token = loginResponse.token,
        filter = MarketFilter(
          eventTypeIds = Set(soccerEvent.get.eventType.id)),
        maxResults = 100
      ))
    }
    soccerEvents <- {
      val soccerEvent = eventTypes.find(eventType => eventType.eventType.name.toLowerCase.contains("soccer"))
      bf.listEvents(AuthorisedRequest(token = loginResponse.token,
        filter = MarketFilter(
          eventTypeIds = Set(soccerEvent.get.eventType.id),
          competitionIds = competitions.map(_.competition.id).toSet,
          marketStartTime = Some(TimeRange.Tomorrow.plusDays(1))),
        maxResults = 100
      ))
    }
    market <- {
      val eventIds = soccerEvents.filter(_.event.name.contains("England")).map(_.event.id)
      bf.listMarketCatalogue(AuthorisedRequest(loginResponse.token,
        filter = MarketFilter(
          eventIds = eventIds.toSet),
        maxResults = 100
      ))
    }
    sorted <- Future(market.sortBy(_.totalMatched)(Ordering[Double].reverse))
    books <- bf.listMarketBook(AuthorisedRequest(loginResponse.token,
      filter = MarketFilter(
        marketIds = sorted.map(_.marketId).toSet),
      maxResults = 100
    ))
  } yield books.map(listMarketBookResponse => sorted.find(_.marketId == listMarketBookResponse.marketId).get.marketName -> listMarketBookResponse)

  soccer.onSuccess {
    case responses => responses foreach println
  }
}

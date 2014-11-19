package uk.co.softsquare.privetng.service

import org.joda.time.DateTime
import play.api.libs.json.Json
import uk.co.softsquare.privetng.WS
import uk.co.softsquare.privetng.auth.Session.{LoginResponse, Token}
import uk.co.softsquare.privetng.auth.{Credentials, Account, Session}
import uk.co.softsquare.privetng.request.{TimeRange, MarketFilter, AuthorisedRequest}
import uk.co.softsquare.privetng.response.{ListMarketBookResponse, ListEventTypesResponse, ListEventsResponse, MarketCatalogueResponse}

import scala.concurrent.{ExecutionContext, Future}

trait Betfair {
  def login(credential: Credentials):                    Future[LoginResponse]
  def listEvents(request: AuthorisedRequest):            Future[List[ListEventsResponse]]
  def listEventTypes(request: AuthorisedRequest):        Future[List[ListEventTypesResponse]]
  def listMarketCatalogue(request: AuthorisedRequest):   Future[List[MarketCatalogueResponse]]
  def listMarketBook(request: AuthorisedRequest):        Future[List[ListMarketBookResponse]]
}

trait BaseEndpoint {
  def executionContext(): ExecutionContext
  implicit val ex: ExecutionContext = executionContext()

  def endpoint(operation: String) = s"https://api.betfair.com/exchange/betting/rest/v1.0/$operation/"
}

trait LoginAction extends BaseEndpoint {
  import uk.co.softsquare.privetng.response._

  val Login = "https://identitysso.betfair.com/api/login"

  def login(credentials: Credentials): Future[LoginResponse] =
    WS.url(Login)
      .withHeaders(
        "X-Application" -> Account.ApplicationKey,
        "Accept" -> "application/json"
      )
      .withRequestTimeout(1000)
      .withFollowRedirects(follow = true)
      .post(Map(
      "username" -> Seq(credentials.username),
      "password" -> Seq(credentials.password))
      ).map(response => response.json.as[LoginResponse])
}

trait ListEventsTypesAction extends BaseEndpoint {
  import uk.co.softsquare.privetng.response._

  val ListEventTypes = endpoint("listEventTypes")

  def listEventTypes(request: AuthorisedRequest): Future[List[ListEventTypesResponse]] =
    WS.urlWithHeaders(ListEventTypes, request.token)
      .post(Json.obj(
      "filter" -> Json.toJson(request.filter)
    )).map(response =>
      response.json.as[List[ListEventTypesResponse]]
    )
}

trait ListEventsAction extends BaseEndpoint {
  import uk.co.softsquare.privetng.response._

  val ListEvents = endpoint("listEvents")

  def listEvents(request: AuthorisedRequest): Future[List[ListEventsResponse]] =
    WS.urlWithHeaders(ListEvents, request.token)
      .post(Json.obj(
      "filter" -> Json.toJson(request.filter)
    )).map(response => response.json.as[List[ListEventsResponse]])
}

trait ListMarketCatalogueAction extends BaseEndpoint {
  import uk.co.softsquare.privetng.response._

  val ListMarketCatalogue = endpoint("listMarketCatalogue")

  def listMarketCatalogue(request: AuthorisedRequest): Future[List[MarketCatalogueResponse]] =
    WS.urlWithHeaders(ListMarketCatalogue, request.token)
      .post(Json.obj(
      "filter" -> Json.toJson(request.filter),
      "maxResults" -> request.maxResults
    )).map(response => response.json.as[List[MarketCatalogueResponse]])
}

trait ListMarketAction extends BaseEndpoint {
  import uk.co.softsquare.privetng.response._

  val ListMarketBook = endpoint("listMarketBook")

  def listMarketBook(request: AuthorisedRequest): Future[List[ListMarketBookResponse]] =
    WS.urlWithHeaders(ListMarketBook, request.token)
      .post(Json.obj(
      "marketIds" -> Json.toJson(request.filter.marketIds)
    )).map(response => response.json.as[List[ListMarketBookResponse]])
}

trait WSBetfair
  extends Betfair
  with LoginAction
  with ListEventsTypesAction
  with ListEventsAction
  with ListMarketCatalogueAction
  with ListMarketAction {

}

object Test extends App {
  val bf = new WSBetfair {
    override def executionContext(): ExecutionContext = ExecutionContext.global
  }
  import bf.ex

  val soccer = for {
    loginResponse <- bf.login(Credentials.fromConsole())
    eventTypes <- bf.listEventTypes(AuthorisedRequest(loginResponse.token))
    soccerEvents <- {
      val soccerEvent = eventTypes.find(eventType => eventType.eventType.name.toLowerCase.contains("soccer"))
      bf.listEvents(AuthorisedRequest(token = loginResponse.token,
        filter = MarketFilter(
          eventTypeIds = Set(soccerEvent.get.eventType.id),
          marketStartTime = Some(TimeRange.Tomorrow.plusDays(1))
        ), maxResults = 100))
    }
    market <- {
      val eventIds = soccerEvents.filter(_.event.name.contains("England")).map(_.event.id)
      bf.listMarketCatalogue(AuthorisedRequest(loginResponse.token,
        MarketFilter(eventIds = eventIds.toSet),
        100
      ))
    }
    sorted <- Future(market.sortBy(_.totalMatched)(Ordering[Double].reverse))
    books <- bf.listMarketBook(AuthorisedRequest(loginResponse.token,
      MarketFilter(marketIds = sorted.map(_.marketId).toSet),
      100
    ))
  } yield books

  soccer.onComplete{result => println(result); sys.exit()}
}
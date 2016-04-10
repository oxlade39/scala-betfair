package uk.co.softsquare.privetng.service

import play.api.libs.json.Json
import uk.co.softsquare.privetng.{HttpComponent, WSHttpComponent}
import uk.co.softsquare.privetng.auth.Session.LoginResponse
import uk.co.softsquare.privetng.auth.Credentials
import uk.co.softsquare.privetng.enums.Wallet
import uk.co.softsquare.privetng.request.{AuthorisedRequest, CancelOrdersRequest, ListMarketBookRequest, MarketFilter, PlaceOrdersRequest, ReplaceOrdersRequest, TimeRange, UpdateOrdersRequest}
import uk.co.softsquare.privetng.response.{ListCompetitionsResponse, ListEventTypesResponse, ListEventsResponse, ListMarketBookResponse, MarketCatalogueResponse}

import scala.concurrent.{ExecutionContext, Future}

trait Betfair {
  def login(credential: Credentials):                    Future[LoginResponse]
  def listEvents(request: AuthorisedRequest):            Future[List[ListEventsResponse]]
  def listEventTypes(request: AuthorisedRequest):        Future[List[ListEventTypesResponse]]
  def listMarketCatalogue(request: AuthorisedRequest):   Future[List[MarketCatalogueResponse]]
  def listMarketBook(request: AuthorisedRequest, body: ListMarketBookRequest): Future[List[ListMarketBookResponse]]
  def listCompetitions(request: AuthorisedRequest):      Future[List[ListCompetitionsResponse]]
}

trait BaseEndpoint {
  def executionContext(): ExecutionContext
  implicit val ex: ExecutionContext = executionContext()

  def endpoint(operation: String) = s"https://api.betfair.com/exchange/betting/rest/v1.0/$operation/"
}

trait LoginAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val Login = "https://identitysso.betfair.com/api/login"

  def login(credentials: Credentials): Future[LoginResponse] =
    http.post[LoginResponse](url = Login, body = Map(
      "username" -> Seq(credentials.username),
      "password" -> Seq(credentials.password))
    )
}

trait ListEventsTypesAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val ListEventTypes = endpoint("listEventTypes")

  def listEventTypes(request: AuthorisedRequest): Future[List[ListEventTypesResponse]] =
    http.postJson[List[ListEventTypesResponse]](
      url = ListEventTypes, token = request.token,
      body = Json.obj("filter" -> Json.toJson(request.filter))
    )
}

trait ListEventsAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val ListEvents = endpoint("listEvents")

  def listEvents(request: AuthorisedRequest): Future[List[ListEventsResponse]] =
    http.postJson[List[ListEventsResponse]](
      url = ListEvents, token = request.token,
      body = Json.obj("filter" -> Json.toJson(request.filter))
    )
}

trait ListMarketCatalogueAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val ListMarketCatalogue = endpoint("listMarketCatalogue")

  def listMarketCatalogue(request: AuthorisedRequest): Future[List[MarketCatalogueResponse]] =
    http.postJson[List[MarketCatalogueResponse]](
      url = ListMarketCatalogue, token = request.token,
      body = Json.obj(
        "filter" -> Json.toJson(request.filter),
        "maxResults" -> request.maxResults
      ))
}

trait ListMarketAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val ListMarketBook = endpoint("listMarketBook")

  def listMarketBook(request: AuthorisedRequest): Future[List[ListMarketBookResponse]] =
    http.postJson[List[ListMarketBookResponse]](
      url = ListMarketBook, token = request.token,
      body = Json.obj("marketIds" -> Json.toJson(request.filter.marketIds))
    )

  def listMarketBook(request: AuthorisedRequest, requestBody: ListMarketBookRequest): Future[List[ListMarketBookResponse]] = {
    http.postJson[List[ListMarketBookResponse]](
      url = ListMarketBook, token = request.token,
      body = Json.toJson(requestBody)
    )
  }
}

trait ListCompetitionsAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val ListCompetitions = endpoint("listCompetitions")

  def listCompetitions(request: AuthorisedRequest): Future[List[ListCompetitionsResponse]] =
    http.postJson[List[ListCompetitionsResponse]](
      url = ListCompetitions, token = request.token,
      body = Json.obj("filter" -> Json.toJson(request.filter))
    )
}

trait PlaceOrdersAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val PlaceOrders = endpoint("placeOrders")

  def placeOrders(request: AuthorisedRequest, requestBody: PlaceOrdersRequest): Future[PlaceExecutionReport] = {
    http.postJson[PlaceExecutionReport](
      url = PlaceOrders, token = request.token,
      body = Json.toJson(requestBody)
    )
  }
}

trait CancelOrdersAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val CancelOrders = endpoint("cancelOrders")

  def cancelOrders(request: AuthorisedRequest, requestBody: CancelOrdersRequest): Future[CancelExecutionReport] = {
    http.postJson[CancelExecutionReport](
      url = CancelOrders, token = request.token,
      body = Json.toJson(requestBody)
    )
  }
}

trait ReplaceOrdersAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val ReplaceOrders = endpoint("replaceOrders")

  def replaceOrders(request: AuthorisedRequest, requestBody: ReplaceOrdersRequest): Future[ReplaceExecutionReport] = {
    http.postJson[ReplaceExecutionReport](
      url = ReplaceOrders, token = request.token,
      body = Json.toJson(requestBody)
    )
  }
}

trait UpdateOrdersAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val UpdateOrders = endpoint("updateOrders")

  def updateOrders(request: AuthorisedRequest, requestBody: UpdateOrdersRequest): Future[UpdateExecutionReport] = {
    http.postJson[UpdateExecutionReport](
      url = UpdateOrders, token = request.token,
      body = Json.toJson(requestBody)
    )
  }
}

trait GetAccountFundsAction extends BaseEndpoint { self: HttpComponent =>
  import uk.co.softsquare.privetng.response._

  val GetAccountFunds = endpoint("getAccountFunds")

  def getAccountFunds(request: AuthorisedRequest, wallet: String = Wallet.Uk): Future[AccountFundsResponse] = {
    http.postJson[AccountFundsResponse](
      url = GetAccountFunds, token = request.token, body = Json.obj("wallet" -> wallet)
    )
  }
}

trait WSBetfair
  extends Betfair
  with LoginAction
  with ListEventsTypesAction
  with ListEventsAction
  with ListMarketCatalogueAction
  with ListMarketAction
  with ListCompetitionsAction
  with PlaceOrdersAction
  with CancelOrdersAction
  with ReplaceOrdersAction
  with UpdateOrdersAction
  with GetAccountFundsAction
  with WSHttpComponent {

}

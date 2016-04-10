package uk.co.softsquare.privetng.service

import uk.co.softsquare.privetng.auth.Credentials
import uk.co.softsquare.privetng.enums.{OrderType, PersistenceType, Side}
import uk.co.softsquare.privetng.request.{AuthorisedRequest, CancelInstruction, CancelOrdersRequest, LimitOrder, PlaceInstruction, PlaceOrdersRequest, ReplaceInstruction, ReplaceOrdersRequest, UpdateInstruction, UpdateOrdersRequest}
import uk.co.softsquare.privetng.response.ExecutionReport

import scala.concurrent.{ExecutionContext, Future}

trait QueryTest {
  val bf = new WSBetfair {
    override def executionContext: ExecutionContext = ExecutionContext.global
  }
  val credentials = Credentials.fromConsole()

  def handle(asyncQuery: Future[ExecutionReport]) = {
    import bf.ex
    asyncQuery.onSuccess {
      case response => println(response)
    }
    asyncQuery.onFailure {
      case error => println(error)
    }
  }
}

object PlaceOrdersQueryTest extends App with QueryTest {
  import bf.ex
  handle(for {
    loginResponse <- bf.login(credentials)
    execReport <- bf.placeOrders(
      request = AuthorisedRequest(token = loginResponse.token),
      requestBody = PlaceOrdersRequest(
        marketId = "1.123456",
        instructions = List(PlaceInstruction(
          side = Side.Back,
          orderType = OrderType.Limit,
          selectionId = 12345,
          limitOrder = Some(LimitOrder(
            size = 2.0,
            price = 4.0,
            persistenceType = PersistenceType.Lapse
          ))
        ))
      ))
  } yield execReport)
}

object CancelOrdersQueryTest extends App with QueryTest {
  import bf.ex
  handle(for {
    loginResponse <- bf.login(credentials)
    execReport <- bf.cancelOrders(
      request = AuthorisedRequest(token = loginResponse.token),
      requestBody = CancelOrdersRequest(
        marketId = "1.123456",
        instructions = List(CancelInstruction(
          betId = "1234567890"
        ))
      ))
  } yield execReport)
}

object ReplaceOrdersQueryTest extends App with QueryTest {
  import bf.ex
  handle(for {
    loginResponse <- bf.login(credentials)
    execReport <- bf.replaceOrders(
      request = AuthorisedRequest(token = loginResponse.token),
      requestBody = ReplaceOrdersRequest(
        marketId = "1.123456",
        instructions = List(ReplaceInstruction(
          betId = "1234567890",
          newPrice = 5.0
        ))
      ))
  } yield execReport)
}

object UpdateOrdersQueryTest extends App with QueryTest {
  import bf.ex
  handle(for {
    loginResponse <- bf.login(credentials)
    execReport <- bf.updateOrders(
      request = AuthorisedRequest(token = loginResponse.token),
      requestBody = UpdateOrdersRequest(
        marketId = "1.123456",
        instructions = List(UpdateInstruction(
          betId = "1234567890",
          newPersistenceType = PersistenceType.Lapse
        ))
      ))
  } yield execReport)
}

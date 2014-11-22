package uk.co.softsquare.privetng

import play.api.libs.json.{Reads, JsValue}
import uk.co.softsquare.privetng.auth.Account

import scala.concurrent.{ExecutionContext, Future}

trait Http {
  def post[T](url: String, body: Map[String, Seq[String]])(implicit fjs: Reads[T]): Future[T]
  def postJson[T](url: String, body: JsValue, token: String)(implicit fjs: Reads[T]): Future[T]
}

trait HttpComponent {
  def executionContext: ExecutionContext
  def http: Http
}

/**
 * Play WS shim to use outside playframework
 */
object WS {
  val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
  val client = new play.api.libs.ws.ning.NingWSClient(builder.build())

  def url(url: String) = client.url(url)
  def urlWithHeaders(endpoint: String, token: String) = url(endpoint).withHeaders(
    "X-Application" -> Account.ApplicationKey,
    "X-Authentication" -> token,
    "content-type" -> "application/json"
  )
}

trait WSHttpComponent extends HttpComponent {
  override def http: Http = new Http {

    implicit val ec = executionContext

    override def post[T](url: String, body: Map[String, Seq[String]])(implicit fjs: Reads[T]): Future[T] =
      WS.url(url)
        .withHeaders(
          "X-Application" -> Account.ApplicationKey,
          "Accept" -> "application/json"
        )
        .withRequestTimeout(1000)
        .withFollowRedirects(follow = true)
        .post(body).map(_.json.as[T])

    override def postJson[T](url: String, body: JsValue, token: String)(implicit fjs: Reads[T]): Future[T] =
      WS.urlWithHeaders(url, token).post(body).map(_.json.as[T])

  }
}
package uk.co.softsquare.privetng

import uk.co.softsquare.privetng.auth.Account

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

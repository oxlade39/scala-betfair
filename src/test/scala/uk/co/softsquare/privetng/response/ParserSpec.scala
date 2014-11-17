package uk.co.softsquare.privetng.response

import org.joda.time.DateTime
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers
import play.api.libs.json.Json

import scala.io.Source

class ParserSpec extends FlatSpec with MustMatchers {
  import uk.co.softsquare.privetng.response.ParserSpec._

  "A Date in Betfair format" should "parse from json" in {
    val date = Json.parse("\"2014-11-16T03:45:00.000Z\"").as[DateTime]
    date.dayOfMonth().get() must be(16)
    date.hourOfDay().get() must be(3)
    date.minuteOfHour().get() must be(45)
  }

  "A ListEventsResponse" should "parse from json" in {
    val response = Json.parse(file("listEvents.json")).as[List[ListEventsResponse]]
    response must not be null
  }

  "A ListEventTypesResponse" should "parse from json" in {
    val response = Json.parse(file("eventTypes.json")).as[List[ListEventTypesResponse]]
    response must not be null
  }

  "A MarketCatalogueResponse" should "parse from json" in {
    val response = Json.parse(file("marketCatalogue.json")).as[List[MarketCatalogueResponse]]
    response must not be null
  }
}

object ParserSpec {
  def file(name: String) =
    Source.fromInputStream(Thread.currentThread().getContextClassLoader.getResourceAsStream(name))
      .getLines()
      .reduce(_ + _)
}
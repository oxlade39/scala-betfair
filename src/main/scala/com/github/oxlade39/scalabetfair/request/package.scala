package com.github.oxlade39.scalabetfair

import org.joda.time.DateTime

/**
 * @author dan
 */
package object request {

  type V5Header = com.betfair.publicapi.types.exchange.v5.APIRequestHeader
  type V3Header = com.betfair.publicapi.types.global.v3.APIRequestHeader

  case class RequestError(message: String)
  case class AllMarketsRequest(event: Event, between: DateRange)
  case class Event(id: Int, name: Option[String] = None)
  case class DateRange(from: DateTime, to: DateTime)
}

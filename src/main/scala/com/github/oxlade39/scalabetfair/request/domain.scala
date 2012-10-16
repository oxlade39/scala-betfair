package com.github.oxlade39.scalabetfair.request

import org.joda.time.DateTime

/**
 * A failed request
 * @param message description of the failure
 */
case class RequestError(message: String)

/**
 * Criteria for a request for all markets
 *
 * @param event the event to request markets for.
 * @param between the date range to select markets for.
 */
case class AllMarketsRequest(event: Event, between: DateRange)

/**
 * An Event on the Betfair exchange
 * @param id the Betfair exchange event id
 * @param name the supplementary (optional) event name
 */
case class Event(id: Int, name: Option[String] = None)

/**
 * An inclusive range of DateTime
 * @param from start of the range
 * @param to end of the range
 */
case class DateRange(from: DateTime, to: DateTime)

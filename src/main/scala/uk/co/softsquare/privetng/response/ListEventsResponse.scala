package uk.co.softsquare.privetng.response

import org.joda.time.DateTime

/**
 *
  {
        "event": {
            "id": "27304139",
            "name": "Arizona State @ Oregon State",
            "countryCode": "US",
            "timezone": "America/Indiana/Indianapolis",
            "openDate": "2014-11-16T03:45:00.000Z"
        },
        "marketCount": 8
    }
 */
case class ListEventsResponse(event: Event, marketCount: Int)
case class Event(id: String, name: String, countryCode: Option[String], timezone: String, openDate: DateTime)
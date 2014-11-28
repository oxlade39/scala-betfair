[![Build Status](https://secure.travis-ci.org/oxlade39/scala-betfair.png)](http://travis-ci.org/oxlade39/scala-betfair)

Betfair API written in Scala
----------------------------

A simple Scala library allowing you to authenticate with Betfair and query the exchange.

All operations operate asynchronously allowing for nice Scala for comprehension to chain and map results from the exchange. 


Example Usage
-------------

### List today's Horse Racing events:

    object TodaysHorseRacingEvents extends ExampleApp {
      import betfair.ex
      
      val HorseRacingEventTypeId = "7"
    
      val horseRacingEvents = for {
        session <- betfair.login(MyCredentials)
        events <- betfair.listEvents(AuthorisedRequest(token = session.token, maxResults = 100, filter = MarketFilter(
          eventTypeIds = Set(HorseRacingEventTypeId),
          marketStartTime = Some(TimeRange.Today)
        )))
      } yield events
    
      horseRacingEvents onSuccess {
        case events =>
          events foreach println
          betfair.http.shutdown()
      }
    }
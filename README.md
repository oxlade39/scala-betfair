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
    
This would output:

    username: ********
    password: ********
    ListEventsResponse(Event(id : 27315292, name : TPara (US) 28th Nov, countryCode : Some(US), timezone : US/Arizona, openDate : 2014-11-28T19:55:00.000Z),2)
    ListEventsResponse(Event(id : 27314820, name : ClubHip (CHL) 28th Nov, countryCode : Some(CL), timezone : America/Santiago, openDate : 2014-11-28T17:30:00.000Z),1)
    ListEventsResponse(Event(id : 27314989, name : Hawth (US) 28th Nov, countryCode : Some(US), timezone : US/Central, openDate : 2014-11-28T19:50:00.000Z),6)
    ListEventsResponse(Event(id : 27315216, name : DeltaD (US) 28th Nov, countryCode : Some(US), timezone : US/Central, openDate : 2014-11-28T23:50:00.000Z),2)
    ListEventsResponse(Event(id : 27315266, name : Penn (US) 28th Nov, countryCode : Some(US), timezone : US/Eastern, openDate : 2014-11-28T23:00:00.000Z),6)
    
### More examples

See the `uk.co.softsquare.privetng.example` package for further examples
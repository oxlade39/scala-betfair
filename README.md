[![Build Status](https://secure.travis-ci.org/oxlade39/scala-betfair.png)](http://travis-ci.org/oxlade39/scala-betfair)

Betfair API written in Scala
----------------------------

Example Usage
-------------
### Create a `BetfairMarketService` :

    // Load from a file '~/.trender/credentials'
    val credentials = Credentials.loadCredentialsFromFS
    // create a BetfairMarketService which caches the Betfair session token for 1 hour
    val betfair = new CachedSessionMarketService(credentials)

### Using a `BetfairMarketService` :

    // search for an Event name containing 'Racing' (like 'Horse Racing')
    // then fetch MarketPrices for races for TodayAndTomorrow
    val pricesOrError = betfair.activeEvents() match {
      case Right(error) => Right(error)
      case Left(activeEvents) => {
        val racing: Option[Event] = activeEvents.find(e => e.name.isDefined && e.name.get.contains("Racing"))
        val markets = racing.map(e => betfair.allMarkets(AllMarketsRequest(e, TodayAndTomorrow)))
        markets match {
          case None => Right(RequestError("no market details"))
          case Some(Left(Nil)) => Right(RequestError("no market details"))
          case Some(Right(error)) => Right(error)
          case Some(Left(marketDetails)) => betfair.marketPrices(marketDetails.head.marketName)
        }
      }
    }
    println(pricesOrError)
    
### More :

See the example code in [RealBetfairMarketServiceSpec.scala](https://github.com/oxlade39/scala-betfair/blob/master/src/test/scala/com/github/oxlade39/scalabetfair/service/RealBetfairMarketServiceSpec.scala)


package com.github.oxlade39.scalabetfair.request

import org.specs2.mutable.Specification
import com.github.oxlade39.scalabetfair.session.SessionProviderComponent
import org.specs2.mock.Mockito
import com.betfair.publicapi.types.global.v3.GetEventTypesReq
import org.joda.time.DateTime
import com.betfair.publicapi.types.exchange.v5.GetAllMarketsReq

/**
 * @author dan
 */
class RequestFactorySpecification extends Specification with Mockito {

  class UnderTest extends WsdlRequestFactoryComponent
                  with HeadersComponent
                  with SessionProviderComponent {

    val sessionProvider = mock[SessionProvider]
    override val headers = mock[Headers]
  }

  "WsdlRequestFactory" should {
    "create an activeEvents request" in {
      val underTest = new UnderTest()

      val v3Header = new V3Header()
      underTest.headers.v3Header returns v3Header

      val request: GetEventTypesReq = underTest.requestFactory.activeEvents

      request.getHeader must_== v3Header
    }

    "create an allMarketsRequest from the given user request" in {
      val underTest = new UnderTest()
      val v5Header = new V5Header()
      underTest.headers.v5header returns v5Header

      val today: DateTime = new DateTime()
      val tomorrow: DateTime = today.plusDays(1)
      val userRequest = AllMarketsRequest(Event(555), DateRange(today, tomorrow))
      val request: GetAllMarketsReq = underTest.requestFactory.allMarkets(userRequest)

      new DateTime(request.getFromDate.toGregorianCalendar).toDate mustEqual today.toDate
      new DateTime(request.getToDate.toGregorianCalendar).toDate mustEqual tomorrow.toDate

      request.getEventTypeIds.getInt.size() mustEqual 1
      request.getEventTypeIds.getInt.get(0) mustEqual 555

      request.getHeader must_== v5Header

      request.getCountries.getCountry.size mustEqual 1
      request.getCountries.getCountry.get(0) mustEqual "GBR"
    }
  }
}

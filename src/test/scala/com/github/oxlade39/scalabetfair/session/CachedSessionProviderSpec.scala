package com.github.oxlade39.scalabetfair.session

import org.specs2.mutable.Specification
import com.github.oxlade39.scalabetfair.date.Dates
import org.joda.time.DateTime
import org.specs2.mock.Mockito

/**
 * @author dan
 */
class CachedSessionProviderSpec extends Specification with Mockito {

//  sequential

  class UnderTest(df: () => DateTime, del: () => String) extends CachedSessionProviderComponent with Dates {
    def dateFactory = df
    def delegate = del
  }

  val now: DateTime = new DateTime()

  "CachedSessionProvider" should {
    "cache the session token for 1 hour" in {

      val mockDateFactory = mock[() => DateTime]
      val mockDelegate = mock[() => String]

      val underTest = new UnderTest(mockDateFactory, mockDelegate)

      mockDateFactory() returns now thenReturns now.plusMinutes(59)
      mockDelegate() returns "session1" thenReturns "session2"

      underTest.sessionProvider.sessionToken mustEqual "session1"
      underTest.sessionProvider.sessionToken mustEqual "session1"

      there was one(mockDelegate).apply()

    }

    "after an hour a new session is requested from the delegate" in {

      val mockDateFactory = mock[() => DateTime]
      val mockDelegate = mock[() => String]

      val underTest = new UnderTest(mockDateFactory, mockDelegate)

      mockDateFactory() returns now thenReturns now.plusMinutes(61)
      mockDelegate() returns "session1"

      underTest.sessionProvider.sessionToken
      underTest.sessionProvider.sessionToken

      there was two(mockDelegate).apply()

    }
  }

}

package com.github.oxlade39.scalabetfair.date

import org.specs2.mutable.Specification
import org.joda.time.{DateTime, DateTimeZone, LocalDate}

/**
 * @author dan
 */
class DatesSpec extends Specification {

  val NOW = new DateTime()

  object Dates extends Dates {
    val dateFactory = () => NOW
  }

  "Dates" should {
    "give me the date today" in {
      Dates.today mustEqual NOW.toLocalDate
    }
    "give me the date tomorrow" in {
      Dates.tomorrow mustEqual NOW.toLocalDate.plusDays(1)
    }
  }
}

class XmlDatesSpec extends Specification {

  val NOW = new DateTime()

  object XmlDates extends XmlDates {
    override def dates = new Dates {
      def dateFactory = () => NOW
    }
  }

  "XmlDates" should {
    "convert LocalDate to XmlGregorianCalendar" in {
      println(XmlDates.tomorrow)
      XmlDates.tomorrow mustEqual XmlDates.tomorrow
    }
  }
}


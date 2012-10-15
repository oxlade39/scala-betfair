package com.github.oxlade39.scalabetfair.date

import org.joda.time.{LocalTime, LocalDate, DateTime}
import javax.xml.datatype.{XMLGregorianCalendar, DatatypeFactory}

/**
 * @author dan
 */

trait Dates {
  def dateFactory: () => DateTime

  /**
   * @return The DateTime as of this instant
   */
  def now: DateTime = dateFactory()

  /**
   * @return the LocalDate today
   */
  def today: LocalDate = now.toLocalDate

  /**
   * @return the LocalDate tomorrow
   */
  def tomorrow: LocalDate = daysFromToday(1)

  /**
   * @return Stream of all LocalDates starting from today incrementing by a day
   */
  def streamOfDaysFromNow: Stream[LocalDate] = Stream.iterate(today)(_.plusDays(1))

  /**
   * @param numDays days in future to select
   * @return the LocalDate numDays from today. I.e daysFromToday(1) is tomorrow
   */
  def daysFromToday(numDays: Int) = streamOfDaysFromNow.drop(1).take(numDays).last
}

trait XmlDates {
  def dates: Dates = new Dates {
    def dateFactory = () => new DateTime()
  }

  lazy val df: DatatypeFactory = DatatypeFactory.newInstance()

  implicit def dateTimeToXMLGregorianCalendar(dtm: LocalDate): XMLGregorianCalendar =
    df.newXMLGregorianCalendar(dtm.toDateTime(LocalTime.MIDNIGHT).toGregorianCalendar)

  def today: XMLGregorianCalendar = dates.today

  def tomorrow: XMLGregorianCalendar = dates.tomorrow
}

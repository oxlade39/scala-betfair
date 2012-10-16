package com.github.oxlade39.scalabetfair

import org.joda.time.{DateTime, LocalTime, LocalDate}
import javax.xml.datatype.{DatatypeFactory, XMLGregorianCalendar}

/**
 * @author dan
 */
package object date {
  lazy val df: DatatypeFactory = DatatypeFactory.newInstance()

  implicit def localDateToXMLGregorianCalendar(dtm: LocalDate): XMLGregorianCalendar =
    dateTimeToXMLGregorianCalendar(dtm.toDateTime(LocalTime.MIDNIGHT))

  implicit def dateTimeToXMLGregorianCalendar(dtm: DateTime): XMLGregorianCalendar =
    df.newXMLGregorianCalendar(dtm.toGregorianCalendar)

}

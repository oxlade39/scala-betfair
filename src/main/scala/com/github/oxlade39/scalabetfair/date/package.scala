package com.github.oxlade39.scalabetfair

import org.joda.time.{LocalTime, LocalDate}
import javax.xml.datatype.{DatatypeFactory, XMLGregorianCalendar}

/**
 * @author dan
 */
package object date {
  lazy val df: DatatypeFactory = DatatypeFactory.newInstance()

  implicit def dateTimeToXMLGregorianCalendar(dtm: LocalDate): XMLGregorianCalendar =
    df.newXMLGregorianCalendar(dtm.toDateTime(LocalTime.MIDNIGHT).toGregorianCalendar)

}

package uk.co.softsquare.privetng.request

import org.joda.time.DateTime

case class TimeRange(from: DateTime, to: DateTime) {
  def plusDays(days: Int) = copy(from = from.plusDays(days), to = to.plusDays(days))
  def plusHours(hours: Int) = copy(from = from.plusHours(hours), to = to.plusHours(hours))
  def plusMinutes(minutes: Int) = copy(from = from.plusMinutes(minutes), to = to.plusMinutes(minutes))

  def minusDays(days: Int) = copy(from = from.minusDays(days), to = to.minusDays(days))
  def minusHours(hours: Int) = copy(from = from.minusHours(hours), to = to.minusHours(hours))
  def minusMinutes(minutes: Int) = copy(from = from.minusMinutes(minutes), to = to.minusMinutes(minutes))
}
case class MarketFilter(eventTypeIds: Set[String] = Set.empty,
                        eventIds: Set[String] = Set.empty,
                        competitionIds: Set[String] = Set.empty,
                        marketIds: Set[String] = Set.empty,
                        marketStartTime: Option[TimeRange] = None)

object TimeRange {
  val Today = TimeRange(from = new DateTime().withTimeAtStartOfDay(), to = new DateTime().withTimeAtStartOfDay().plusDays(1))
  val Tomorrow = Today.plusDays(1)
  val TheNextHour = TimeRange(from = new DateTime(), to = new DateTime().plusHours(1))

  def nextDays(days: Int) = TimeRange(from = new DateTime(), to = new DateTime().plusDays(days))
}

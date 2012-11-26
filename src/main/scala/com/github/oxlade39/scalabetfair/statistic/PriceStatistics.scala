package com.github.oxlade39.scalabetfair.statistic

import com.github.oxlade39.scalabetfair.math.BigDecimalMath

trait PriceStatistics {
  def currentPrice: BigDecimal
  def average: BigDecimal
  def standardDeviation: BigDecimal
  def variance: BigDecimal
  def momentum: BigDecimal
  def rateOfChange: BigDecimal

  override def hashCode() =
    average.hashCode() + standardDeviation.hashCode() +
      variance.hashCode() + momentum.hashCode() +
      rateOfChange.hashCode()

  override def equals(p1: Any) =
    if (!p1.isInstanceOf[PriceStatistics]) false
    else {
      val other = p1.asInstanceOf[PriceStatistics]

      val otherAverage: BigDecimal = other.average
      val otherSD: BigDecimal = other.standardDeviation
      val otherVariance: BigDecimal = other.variance
      val otherMomentum: BigDecimal = other.momentum
      val otherRoC: BigDecimal = other.rateOfChange

      val myAverage = average
      val mySD = standardDeviation
      val myVariance = variance
      val myMomentum = momentum
      val myROC = rateOfChange
      otherAverage.equals(myAverage) &&
        otherSD.equals(mySD) &&
        otherVariance.equals(myVariance) &&
        otherMomentum.equals(myMomentum) &&
        otherRoC.equals(myROC)
    }
}

trait PriceHistory extends PriceStatistics {
  def add(toAdd: BigDecimal): PriceHistory
  def size: Int

  override def toString = "PriceHistory[average = %s of %s prices]".format(average, size)
}

object PriceHistory {
  def apply(maxSize: Int = 4): PriceHistory = {
    val initialItems = 1.to(maxSize).toSeq.map(i => BigDecimal(0))
    new InternalPriceHistory(initialItems, 0, maxSize, 0)
  }

  implicit def fromList(prices: List[BigDecimal]): PriceHistory =
    prices.reverse.foldLeft(PriceHistory(maxSize = prices.size))(_.add(_))
}

private class InternalPriceHistory(values: Seq[BigDecimal], val size: Int, maxSize: Int, total: BigDecimal)
  extends PriceHistory {

  val currentPrice = values.head

  lazy val average: BigDecimal = if(size == 0) 0 else total / size

  lazy val standardDeviation: BigDecimal = variance match {
    case zero if(zero == BigDecimal(0)) => BigDecimal(0)
    case nonZero => BigDecimalMath.sqrt(nonZero)
  }
  lazy val variance: BigDecimal = squareDifferences.sum / squareDifferences.size
  lazy val momentum: BigDecimal = values.head - last
  lazy val rateOfChange: BigDecimal = last match {
    case zero if(zero == BigDecimal(0)) => BigDecimal(0)
    case nonZero => (values.head - nonZero) / nonZero
  }

  private[this] lazy val last = values(size - 1)

  private[this] lazy val squareDifferences: Seq[BigDecimal] =
    values.slice(0, size).map(v => differenceFromAverage(v).pow(2))

  private[this] def differenceFromAverage(value: BigDecimal): BigDecimal = value - average

  def add(toAdd: BigDecimal) = {
    val oldPrice = values(maxSize - 1)
    val newTotal = (total - oldPrice) + toAdd
    val newSize = if(size == maxSize) maxSize else size + 1

    val newValues = values.zipWithIndex.map {
      case (value, i) if(i == 0) => toAdd
      case (value, i) => values(i - 1)
    }
    new InternalPriceHistory(newValues, newSize, maxSize, newTotal)
  }

}
package com.github.oxlade39.scalabetfair.statistic

import org.specs2.mutable.Specification
import com.github.oxlade39.scalabetfair.math.BigDecimalMath.toBigDecimal
import com.github.oxlade39.scalabetfair.math.BigDecimalMath
import PriceHistory.fromList

/**
 * @author dan
 */
class PriceStatisticsSpec extends Specification {
  "PriceHistory" should {
    "has a currentPrice equal to the last added price" in {
      var values = PriceHistory(maxSize = 10)
        .add("1.13")
        .add("1.14")
        .add("1.15")

      values.currentPrice mustEqual BigDecimal("1.15")
    }

    "have an average of the list of values" in {
      var values = PriceHistory(maxSize = 10)
                    .add("1.11")
                    .add("1.12")
                    .add("1.13")
                    .add("1.14")
                    .add("1.15")

      var expectedAverage: BigDecimal = (BigDecimal("1.11") + "1.12" + "1.13" + "1.14" + "1.15") / "5"
      values.average mustEqual (expectedAverage)

      values = PriceHistory(maxSize = 10)
                .add(1)
                .add(2)
                .add(3)
                .add(1)
                .add(2)
                .add(3)
      expectedAverage = (BigDecimal(1) + "2" + "3" + "1" + "2" + "3") / 6
      values.average mustEqual (2)
    }

    "have a standard deviation of the list of values" in {
      val values = BigDecimal(1) :: BigDecimal(2) :: BigDecimal(3) ::
          BigDecimal(1) :: BigDecimal(2) :: BigDecimal(3) :: Nil

      val average = BigDecimal(2)
      val sumOfDifferencesSquared: BigDecimal =
        ((BigDecimal(1) - average).pow(2)) + ((BigDecimal(2) - average).pow(2)) + ((BigDecimal(3) - average).pow(2)) +
          ((BigDecimal(1) - average).pow(2)) + ((BigDecimal(2) - average).pow(2)) + ((BigDecimal(3) - average).pow(2))

      val variance: BigDecimal = sumOfDifferencesSquared / 6
      val expectedStandardDeviation: BigDecimal = BigDecimalMath.sqrt(variance)

      values.standardDeviation mustEqual(expectedStandardDeviation)
    }

    "have a momentum of the first minus the last value" in {
      (BigDecimal(5) :: BigDecimal(4) :: BigDecimal(3)
        :: BigDecimal(2) :: BigDecimal(1) :: Nil).momentum mustEqual(4)

      (BigDecimal(10) :: BigDecimal(8) :: BigDecimal(6)
        :: BigDecimal(4) :: BigDecimal(2) :: Nil).momentum mustEqual(8)
    }

    "have a rate of change equal (first - last) / last value" in  {
      (BigDecimal(5) :: BigDecimal(4) :: BigDecimal(3)
        :: BigDecimal(2) :: BigDecimal(1) :: Nil).rateOfChange mustEqual(4)

      (BigDecimal(1) :: BigDecimal(2) :: BigDecimal(3)
        :: BigDecimal(4) :: BigDecimal(5) :: Nil).rateOfChange mustEqual(-0.8)
    }

    "handle zero variance" in {

      val noVariance = BigDecimal("1.55") :: BigDecimal("1.55") :: Nil
      noVariance.variance mustEqual(BigDecimal(0))
      noVariance.standardDeviation mustEqual(BigDecimal(0))

    }

    "give the rate of change as first minus last over last" in {
      var priceHistory = PriceHistory()
        .add(BigDecimal(1))
        .add(BigDecimal(2))
        .add(BigDecimal(3))
        .add(BigDecimal(4))

      val expectedRateOfChange = (BigDecimal(4) - 1) / 1

      expectedRateOfChange mustEqual priceHistory.rateOfChange
    }

    "is equal to the same history of prices but of different max size" in {
      val maxSize10 = PriceHistory(maxSize = 10)
        .add(1)
        .add(2)
        .add(3)

      val maxSize4 = PriceHistory()
        .add(1)
        .add(2)
        .add(3)

      maxSize10 mustEqual maxSize4
    }
  }
}

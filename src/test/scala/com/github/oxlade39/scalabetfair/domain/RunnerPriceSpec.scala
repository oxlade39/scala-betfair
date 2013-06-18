package com.github.oxlade39.scalabetfair.domain

import org.specs2.mutable.Specification
import com.github.oxlade39.scalabetfair.math.BigDecimalMath.toBigDecimal

class RunnerDetailSpec extends Specification {
  val testRunner = RunnerDetail(Runner("runner", 1), 12, 1000,
    RunnerPrice(90, 0, 10) :: RunnerPrice(1, 100, 0) :: RunnerPrice(50, 10, 0) :: RunnerPrice(1000, 0, 5) :: Nil)

  "RunnerDetail" should {
    "list the bestBacks in order of highest price first filtering not available" in {
      testRunner.bestBacks mustEqual RunnerPrice(50, 10, 0) :: RunnerPrice(1, 100, 0) :: Nil
    }

    "list the bestLays in order of lowest price first filtering not available" in {
      testRunner.bestLays mustEqual RunnerPrice(90, 0, 10) :: RunnerPrice(1000, 0, 5) :: Nil
    }
  }
}

class RunnerPriceSpec extends Specification {
  "RunnerPrice" should {
    "be backable if there is a non zero backAvailable" in {
      val price: RunnerPrice = RunnerPrice(100, 1, 0)
      price.isBackable mustEqual true
      RunnerPrice(100, "1.5", 0).isBackable mustEqual true
      RunnerPrice(100, "0.5", 0).isBackable mustEqual true
      RunnerPrice(100, 0, 0).isBackable mustEqual false
      RunnerPrice(100, 0, 1).isBackable mustEqual false
    }

    "be layable if there is a non zero backAvailable" in {
      RunnerPrice(100, 0, 1).isLayable mustEqual true
      RunnerPrice(100, 0, "1.5").isLayable mustEqual true
      RunnerPrice(100, 1, "0.1").isLayable mustEqual true

      RunnerPrice(100, 1, 0).isLayable mustEqual false
      RunnerPrice(100, 100, 0).isLayable mustEqual false
      RunnerPrice(100, 0, 0).isLayable mustEqual false
    }
  }
}

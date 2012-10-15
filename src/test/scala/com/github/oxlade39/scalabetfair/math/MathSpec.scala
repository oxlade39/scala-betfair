package com.github.oxlade39.scalabetfair.math

import org.specs2.mutable.Specification
import java.math.MathContext

class MathSpec extends Specification {
  "BigDecimalMath" should {
    "calculate the square root" in {
      BigDecimalMath.sqrt(BigDecimal(25)) mustEqual(BigDecimal(5))
      BigDecimalMath.sqrt(BigDecimal(9)) mustEqual(BigDecimal(3))
      BigDecimalMath.sqrt(BigDecimal(2, new MathContext(9))) mustEqual(BigDecimal("1.41421356"))
    }
  }
}

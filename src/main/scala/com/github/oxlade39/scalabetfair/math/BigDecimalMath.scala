package com.github.oxlade39.scalabetfair.math

object BigDecimalMath {
  implicit def toBigDecimal(decimal: String): BigDecimal = BigDecimal(decimal)

  def sqrt(x: BigDecimal): BigDecimal = {
    val maxIterations = x.mc.getPrecision + 1

    val guessSteam: Stream[BigDecimal] = newtonRaphsonApproximations(x).take(maxIterations)
    val exactMatch: Option[Stream[BigDecimal]] = guessSteam.sliding(2).find(a => a(0) == a(1))
    val root: Stream[BigDecimal] = exactMatch.getOrElse(Stream(guessSteam.last))

    root(0)
  }

  /**
   * A sequence of BigDecimals the tend towards the square root of toSqrt.
   * Using the Newton Raphson Approximations http://en.wikipedia.org/wiki/Newton's_method
   * @param toSqrt the value to find the root of
   * @param guess the first guess to iterate over (typically toSqrt/2)
   * @return
   */
  private[this] def newtonRaphsonApproximations(toSqrt: BigDecimal, guess: BigDecimal): Stream[BigDecimal] =
    Stream.cons(guess, newtonRaphsonApproximations(toSqrt, ((toSqrt / guess) + guess) / 2))

  private[this] def newtonRaphsonApproximations(toSqrt: BigDecimal): Stream[BigDecimal] =
    newtonRaphsonApproximations(toSqrt, toSqrt / 2)

}

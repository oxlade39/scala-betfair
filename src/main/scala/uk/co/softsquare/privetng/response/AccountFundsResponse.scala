package uk.co.softsquare.privetng.response

import uk.co.softsquare.privetng.util.ReflectiveToString

case class AccountFundsResponse(availableToBetBalance: Option[Double],
                               exposure: Option[Double],
                               retainedCommission: Option[Double],
                               exposureLimit: Option[Double],
                               discountRate: Option[Double],
                               pointsBalance: Option[Int]) extends ReflectiveToString

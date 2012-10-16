package com.github.oxlade39.scalabetfair.response

import org.specs2.mutable.Specification
import com.betfair.publicapi.types.exchange.v5.{Market => BfMarket, Runner => BfRunner, ArrayOfRunner, GetMarketErrorEnum, GetMarketResp}
import com.github.oxlade39.scalabetfair.request.RequestError
import com.github.oxlade39.scalabetfair.domain.Runner

/**
 * @author dan
 */
class ResponseParserSpec extends Specification {

  val underTest = new RealResponseParserComponent {}.responseParser

  "ResponseParser" should {
    "return a request error if GetMarketResp has errors" in {

      val betfairResponse: GetMarketResp = new GetMarketResp()
      betfairResponse.setErrorCode(GetMarketErrorEnum.INVALID_MARKET)
      betfairResponse.setMinorErrorCode("no such market")

      val market = underTest.runnersFromMarket(betfairResponse)

      market mustEqual Right(RequestError("API error: %s:%s".format(GetMarketErrorEnum.INVALID_MARKET.value(), "no such market")))
    }

    "return a list of runners from GetMarketResp" in {

      val betfairResponse: GetMarketResp = new GetMarketResp()
      betfairResponse.setErrorCode(GetMarketErrorEnum.OK)
      val bfMarket = new BfMarket()
      val bfRunners = new ArrayOfRunner()
      bfRunners.getRunner.add(createRunner(1))
      bfRunners.getRunner.add(createRunner(2))
      bfMarket.setRunners(bfRunners)

      betfairResponse.setMarket(bfMarket)

      val market = underTest.runnersFromMarket(betfairResponse)

      market mustEqual Left(List(Runner("runner1", 1), Runner("runner2", 2)))
    }
  }

  def createRunner(id: Int) = {
    val runner = new BfRunner()
    runner.setName("runner%s".format(id))
    runner.setSelectionId(id)
    runner
  }
}

package uk.co.softsquare.privetng

import java.util.concurrent.Executor

import uk.co.softsquare.privetng.auth.Credentials
import uk.co.softsquare.privetng.service.{Betfair, WSBetfair}

import scala.concurrent.ExecutionContext

package object example {
  trait ExampleApp extends App with Example

  trait Example {
    import uk.co.softsquare.privetng.example.Example.CurrentThreadExecutionContext

    val betfair = new WSBetfair {
      override def executionContext: ExecutionContext = CurrentThreadExecutionContext
    }

    lazy val MyCredentials = Credentials.fromConsole()
  }

  object Example {
    val CurrentThreadExecutionContext = ExecutionContext.fromExecutor(new Executor {
      override def execute(command: Runnable): Unit = command.run()
    })
  }
}

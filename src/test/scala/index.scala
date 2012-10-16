/**
 * @author dan
 */
import org.specs2._
import runner.SpecificationsFinder._

class index extends Specification { def is =

  examplesLinks("Scala Betfair")

  // see the SpecificationsFinder trait for the parameters of the 'specifications' method
  def examplesLinks(t: String) = specifications().foldLeft(t.title) { (res, cur) => res ^ see(cur) }
}

package mock

import com.intuit.karate.gatling.PreDef.{pauseFor, _}
import io.gatling.core.Predef._

import scala.concurrent.duration._

class CatsKarateSimulation extends Simulation {

  MockUtils.startServer()

  val protocol = karateProtocol(
    "/cats/{id}" -> Nil,
    "/cats" -> pauseFor("get" -> 15, "post" -> 25)
  )

  val create = scenario("create").exec(karateFeature("classpath:mock/cats-create.feature"))
  val delete = scenario("delete").exec(karateFeature("classpath:mock/cats-delete.feature"))

  setUp(
    create.inject(rampUsers(10) over (5 seconds)).protocols(protocol),
    delete.inject(rampUsers(5) over (5 seconds)).protocols(protocol)
  )

}

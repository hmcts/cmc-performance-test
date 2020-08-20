package uk.gov.hmcts.reform.cmc.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.reform.cmc.performance.utils.Environment

class CMCSimulation extends Simulation
     {

       val httpProtocol: HttpProtocolBuilder = http
         //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
         //.proxy(Proxy("proxyout.reform.hmcts.net", 8080))
         .baseUrl(Environment.cmcBashURL)
         .headers(Environment.commonHeader)

       val legalhttpProtocol: HttpProtocolBuilder = http
         //.proxy(Proxy("proxyout.reform.hmcts.net", 8080).httpsPort(8080))
         //.proxy(Proxy("proxyout.reform.hmcts.net", 8080))
         .baseUrl(Environment.cmcLegalURL)
         .headers(Environment.commonHeader)

       implicit val postHeaders: Map[String, String] = Map(
         "Origin" -> Environment.cmcBashURL
       )
       val scenario1 = scenario("Create Claim Journey")
         .exec(CreateClaimSimulation.createClaimScenario)

       val scenario3 = scenario("Delete User Journey")
         .exec(CreateClaimSimulation.deleteUserScenario)

       val scenario2 = scenario("Defendant Journey")
         .exec(CreateDefendantSimulation.createDefendantScenario)

       val scenario_LC = scenario("Legal Rep Journey With Claims")
         .exec(CreateLegalSimulation.createLegalClaimScenario)

       val scenario_NLC = scenario("Legal Rep Journey Without Claims")
         .exec(CreateLegalSimulation.createLegalNoClaimScenario)


       // NIGHTLY TEST RUNS ///////////

       setUp(
         scenario1.inject(rampUsers(Environment.users) during (200))
           .protocols(httpProtocol),
         scenario_LC.inject(rampUsers(Environment.users) during  (200))
           .protocols(legalhttpProtocol),
       )
         .maxDuration(3700)


       // FULL TEST RUNS ///////////

       /*setUp(
         scenario1.inject(
           atOnceUsers(100)).protocols(httpProtocol))*/

         /*setUp(scenario1
           .inject(
           rampUsers(1).over(10 seconds))
           .protocols(httpProtocol))
           .maxDuration(90 minutes)
           .assertions(
               global.responseTime.max.lt(5000),
               forAll.failedRequests.count.lt(20)*/


       /*setUp(scenario1
         .inject(
           rampUsers(1) during (200))
         .protocols(httpProtocol))
         .maxDuration(5400)*/
        /* .assertions(
           global.responseTime.max.lt(5000),
           forAll.failedRequests.count.lt(20)
         )*/

      /* setUp(scenario1
         .inject(
           rampUsers(714) during  (1600))
         .protocols(httpProtocol))
         .maxDuration(7200)*/

       /* setUp(
         scenario_LC.inject(rampUsers(56) during  (2700))
           .protocols(legalhttpProtocol),
         scenario_NLC.inject(nothingFor(100),rampUsers(100) during  (2600))
           .protocols(legalhttpProtocol)
       )
         .maxDuration(3700) */

     }

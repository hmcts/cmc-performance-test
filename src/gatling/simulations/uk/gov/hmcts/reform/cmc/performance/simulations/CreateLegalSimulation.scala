package uk.gov.hmcts.reform.cmc.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.reform.cmc.performance.legalprocesses._
import uk.gov.hmcts.reform.cmc.performance.utils.Environment
//import uk.gov.hmcts.reform.cmc.performance.simulations.lifecycle.SimulationHooks
//import uk.gov.hmcts.reform.idam.{LoginPage, User}

object CreateLegalSimulation {
//  testUsers = List(User.legal)

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(Environment.cmcLegalURL)
    .inferHtmlResources(BlackList(), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate, sdch, br")
    .header("Upgrade-Insecure-Requests", "1")

  implicit val postHeaders: Map[String, String] = Map(
    "Origin" -> Environment.cmcLegalURL
  )

  val feeder = csv("IdamUsers.csv").circular
  val feeder_noclaim = csv("IdamUsers_noclaim.csv").circular

  val createLegalClaimScenario=feed(feeder).
    exec(
     // LoginPage.legalLogIn(testUsers.head),
      LegalLoginPage.legalLogIn,
      ClaimantLegalRepresentative.run,
      ClaimantDetails.run,
      DefendantDetails.run,
      AmountClaimDetails.run,
        LegalLoginPage.legalLogout
    )

  val createLegalNoClaimScenario=feed(feeder_noclaim).
    exec(
      LegalLoginPage.legalLogIn,
      ClaimantLegalRepresentative.run,
      ClaimantDetails.run,
      DefendantDetails.run,
      LegalLoginPage.legalLogout
    )

  /*setUp(createLegalClaimScenario
    .inject(rampUsers(10).over(10 seconds))
    .protocols(httpProtocol))
    .maxDuration(10 minutes)
    .assertions(
      global.responseTime.max.lt(5000),
      forAll.failedRequests.count.lt(1)
    )
*/
}

package uk.gov.hmcts.reform.cmc.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.reform.cmc.performance.defendantprocess
import uk.gov.hmcts.reform.cmc.performance.defendantprocess.LandingPage
import uk.gov.hmcts.reform.cmc.performance.defendantprocess.LoginDefendantPage
import uk.gov.hmcts.reform.cmc.performance.processes._
import uk.gov.hmcts.reform.cmc.performance.defendantprocess._

import scala.concurrent.duration._
//import uk.gov.hmcts.reform.cmc.performance.simulations.lifecycle.SimulationHooks
//import uk.gov.hmcts.reform.idam.{User,LoginPage}

import uk.gov.hmcts.reform.cmc.performance.utils._

object CreateDefendantSimulation {

  val WaitForNextIteration = Environment.waitForNextIteration
  
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(Environment.cmcBashURL)
    .headers(Environment.commonHeader)

  implicit val postHeaders: Map[String, String] = Map(
    "Origin" -> Environment.cmcBashURL
  )

  val createDefendantScenario=
      exec(
        LandingPage.landingPage,
        LandingPage.startPage,//  LoginPage.logIn(testUsers.head),
        LoginDefendantPage.claimNumber,
        LoginDefendantPage.enterCode,
        LoginDefendantPage.ClaimSummary,
        LoginDefendantPage.receiver_get,
        LoginDefendantPage.loginAsDefendant,
        DefendantDetails.dashBoard,
        DefendantDetails.casetaskList,
        DefendantDetails.defendantDetails,
        DefendantDetails.dob,
        DefendantDetails.mobile,
        DefendantDetails.moreTimeRequest,
        DefendantDetails.responseType,
        DefendantDetails.reject_All_Claims,
        DefendantDetails.yourDefence,
        DefendantDetails.timeLine,
        DefendantDetails.evidence,
        DefendantDetails.freeMediation,
        DefendantDetails.checkAndSend

      )
      
      pace(WaitForNextIteration)

 /* setUp(createDefendantScenario
    .inject(rampUsers(1).over(5 seconds))
    .protocols(httpProtocol))*/


}

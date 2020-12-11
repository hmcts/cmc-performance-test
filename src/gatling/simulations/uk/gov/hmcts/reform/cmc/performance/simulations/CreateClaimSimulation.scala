package uk.gov.hmcts.reform.cmc.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.reform.cmc.performance.processes._
//import uk.gov.hmcts.reform.cmc.performance.simulations.lifecycle.SimulationHooks
//import uk.gov.hmcts.reform.idam.{User,LoginPage}

import uk.gov.hmcts.reform.cmc.performance.utils._

object  CreateClaimSimulation {

  val WaitForNextIteration = Environment.waitForNextIteration
  val idamBaseUrl=Environment.idamBaseURL
  val createIdamUsersFeeder = Feeders.createIdamUsersFeeder
  val addIdamUserUrl = idamBaseUrl + "/testing-support/accounts"
  val feeder_json_citizen=jsonFile("citizen_data.json").circular


  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(Environment.cmcBashURL)
    .headers(Environment.commonHeader)

  implicit val postHeaders: Map[String, String] = Map(
    "Origin" -> Environment.cmcBashURL
  )


  val createUsers = feed(createIdamUsersFeeder).exec(http("Create IDAM users")
    .post(addIdamUserUrl)
    .body(StringBody("${addUser}")).asJson
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(201)))
    .pause(60)



  def createClaimScenario =

    //createUsers
        exec(
        LoginPage.logIn, //  LoginPage.logIn(testUsers.head),
        Eligibility.run,
        ResolvingThisDispute.run,
        CompletingYourClaim.run,
        YourDetails.run,
        TheirDetails.run,
        Amount.run,
        Reason.run,
        CheckAndSend.run
        //LoginPage.deleteUser
      )

  //def deleteUserScenario =

  // createUsers
    //exec(

      //LoginPage.deleteUser
    //)


  //pace(WaitForNextIteration)



}

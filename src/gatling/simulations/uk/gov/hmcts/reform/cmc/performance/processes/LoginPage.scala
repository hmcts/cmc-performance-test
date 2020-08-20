package uk.gov.hmcts.reform.cmc.performance.processes

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.cmc.performance.simulations.CreateClaimSimulation.addIdamUserUrl
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.{CsrfCheck, CurrentPageCheck}
//import uk.gov.hmcts.reform.idam.User

import uk.gov.hmcts.reform.cmc.performance.utils._

object LoginPage {

  val thinktime = Environment.thinkTime

    val feeder = csv("IdamUserDataPreprodEnv.csv").circular

  //def logIn(user: User)(implicit postHeaders: Map[String, String]): ChainBuilder = {
 val logIn =   feed(feeder).exec(http("TX01_CMC_Login_LandingLoginPage")
      .get("/")
      .check(CurrentPageCheck.save)
      .check(CsrfCheck.save)
   .check(status.is(200))
      .check(regex("Email address")))
     .exitHereIfFailed
      .pause(thinktime)
      .exec(http("TX02_CMC_Login_SubmitLogin")
        .post("${currentPage}")
      //  .headers(headers_9)
        .formParam("username", "${iaDMUserName}")  //Demo - Kapil.Jain@hmcts.net , Non Prod civilmoneyclaims+vivekcmct1@gmail.com
        .formParam("password", "Pass19word")
       // .formParamMap(Map("username" -> "${generatedEmail}", "password" -> "${generatedPassword}"))
        .formParam("_csrf", "${csrf}")
        .check(regex("(Find out if you can make a claim using this service)|(Your money claims account)")))
     .exitHereIfFailed
      .pause(thinktime)
 // }

  val citizenLogout = exec(http("TX050_CMC_Logout")
    .get("/logout")
    .check(regex("Sign in")))
    .pause(thinktime)



  //def legalLogIn(user: User)(implicit postHeaders: Map[String, String]): ChainBuilder = {
   val legalLogIn =  exec(http("Trigger login")
      .get("/")
      .check(CurrentPageCheck.save)
      .check(CsrfCheck.save)
      .check(css(".form-group>input[name='client_id']", "value").saveAs("clientId"))
      .check(css(".form-group>input[name='state']", "value").saveAs("state"))
      .check(css(".form-group>input[name='redirect_uri']", "value").saveAs("redirectUri"))
      .check(css(".form-group>input[name='continue']", "value").saveAs("continue")))
      .pause(thinktime)
      .exec(http("Login - legal submit")
        .post("${currentPage}")
     //   .headers(postHeaders)
       // .formParam("username", "civilmoneyclaims+vivekcmct1@gmail.com")
       // .formParam("password", "Password1234")
        .formParamMap(Map("username" -> "${generatedEmail}", "password" -> "${generatedPassword}"))
        .formParam("response_type", "code")
        .formParam("continue", "${continue}") // tactical idam maps redirect_uri to continue =/
        .formParam("upliftToken", "")
        .formParam("_csrf", "${csrf}")
        .formParam("redirect_uri", "${redirectUri}")
        .formParam("client_id", "${clientId}")
        .formParam("scope", "")
        .formParam("state", "${state}")
        .check(regex("Issue civil court proceedings"))
      )
      .pause(thinktime)

  val deleteUser = exec(http("Delete IDAM users")
    .delete(addIdamUserUrl+"/${iaDMUserName}")
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(204)))
    .pause(20)

}

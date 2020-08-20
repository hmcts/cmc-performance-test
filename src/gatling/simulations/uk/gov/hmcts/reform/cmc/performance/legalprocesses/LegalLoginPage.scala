package uk.gov.hmcts.reform.cmc.performance.legalprocesses

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.{CsrfCheck, CurrentPageCheck}
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}
//import uk.gov.hmcts.reform.idam.User

import uk.gov.hmcts.reform.cmc.performance.utils._

object LegalLoginPage {

  val thinktime = Environment.thinkTime

    val feeder = csv("IdamUsers.csv").circular

  //def legalLogIn(user: User)(implicit postHeaders: Map[String, String]): ChainBuilder = {
   val legalLogIn =  exec(http("CLR01_010_Homepage")
      .get("/")
     .check(status.in(200,201,204))
      .check(CurrentPageCheck.save)
      .check(CsrfCheck.save)
   ).exitHereIfFailed
     .pause(thinktime)
      .exec(http("CLR01_020_Login")
        .post("${currentPage}")
        .formParam("username", "${idamUser}")
        .formParam("password", "Pass19word")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "true")
        .formParam(csrfParameter, csrfTemplate)
        .check(CurrentPageCheck.save)
        .check(CsrfCheck.save)
       // .check(regex("Issue civil court proceedings"))
      )
      .pause(thinktime)

  /*val deleteUser = exec(http("Delete IDAM users")
    .delete(addIdamUserUrl+"/${iaDMUserName}")
    .headers(Map("Content-Type" -> "application/json"))
    .check(status.is(204)))
    .pause(20)*/

  val legalLogout=
  exec(http("CLR01_0330_HomepageTX040_Logout")
    .get("/logout")
    .check(status.in(200,201,204))
  )
    .pause(thinktime)

}

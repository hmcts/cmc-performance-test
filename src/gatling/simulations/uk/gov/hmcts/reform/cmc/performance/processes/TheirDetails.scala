package uk.gov.hmcts.reform.cmc.performance.processes

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CurrentPageCheck.currentPageTemplate
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.{CsrfCheck, CurrentPageCheck}
import uk.gov.hmcts.reform.cmc.performance.utils._

object TheirDetails {

  val thinktime = Environment.thinkTime
  
  def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
    exec(http("TX024_CMC_TheirDetail_PartyTypeSelection_GET")
      .get("/claim/defendant-party-type-selection")
      .check(CsrfCheck.save)
      .check(CurrentPageCheck.save)
       .check(regex("Who are you making the claim against")))
      .pause(thinktime)
      
      .exec(http("TX025_CMC_TheirDetail_PartyTypeSelection_POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("type", "individual")
       // .formParam("saveAndContinue", "Save and continue")
        .check(CsrfCheck.save)
        .check(CurrentPageCheck.save)
        .check(regex("Enter the defendant’s details")))
      .pause(thinktime)
      
      .exec(http("TX026_CMC_TheirDetail_TheirDetails_POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
       // .formParam("name", "John Smith")
        .formParam("title", "MR")
        .formParam("firstName", "Geoff")
        .formParam("lastName", "Green")
        .formParam("address[postcodeLookup]", "EC1M 3JB")
        .formParam("address[addressList]", """{"addressLines":["221B","BAKER STREET",""],"townOrCity":"LONDON","postCode":"NW1 6XE"}""")
        .formParam("address[line1]", "221B")
        .formParam("address[line2]", "BAKER STREET")
        .formParam("address[line3]", "")
        .formParam("address[city]", "LONDON")
        .formParam("address[postcode]", "NW1 6XE")
        .formParam("address[addressVisible]", "true")
        .formParam("address[addressSelectorVisible]", "false")
        .formParam("address[enterManually]", "false")
        .formParam("saveAndContinue", "Save and continue")
        .check(CsrfCheck.save)
        .check(CurrentPageCheck.save)
         .check(regex("Their email address")))
      .pause(thinktime)

      
      .exec(http("TX027_CMC_TheirDetail_EmailAddress_POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("address", "ccd-defend2@paulnisbett.com")
        .formParam("saveAndContinue", "Save and continue")
        .check(regex("Their phone number"))
      )
      .pause(thinktime)

      .exec(http("TX027_CMC_TheirDetail_Mobile_POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("number", "")
        .formParam("saveAndContinue", "Save and continue")
      //  .check(regex("Make a money claim"))
      )
      .pause(thinktime)
  }

}

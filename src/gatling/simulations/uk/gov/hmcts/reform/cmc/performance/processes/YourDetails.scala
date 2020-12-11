package uk.gov.hmcts.reform.cmc.performance.processes

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CurrentPageCheck.currentPageTemplate
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.{CsrfCheck, CurrentPageCheck}
import uk.gov.hmcts.reform.cmc.performance.utils._

object YourDetails {

  val thinktime = Environment.thinkTime
  
  def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
    exec(http("TX019_CMC_YourDetail_PartyTypeSelection-GET")
      .get("/claim/claimant-party-type-selection")
      .check(CsrfCheck.save)
      .check(CurrentPageCheck.save)
      .check(regex("About you and this claim")))
      .pause(thinktime)
      
      .exec(http("TX020_CMC_YourDetail_PartyTypeSelection-POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("type", "individual")
        .formParam("saveAndContinue", "Save and continue")
        .check(CsrfCheck.save)
        .check(CurrentPageCheck.save)
        .check(regex("your details")))
      .pause(thinktime)
      
      .exec(http("TX021_CMC_YourDetail_YourDetails-POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("name", "Paul Nisbett")
        .formParam("address[postcodeLookup]", "")
        .formParam("address[line1]", "3 David's walk")
        .formParam("address[line2]", "")
        .formParam("address[line3]", "")
          .formParam("address[city]", "Billericay")
        .formParam("address[postcode]", "CM11 2EH")
        .formParam("address[addressVisible]", "true")
			 .formParam("address[addressSelectorVisible]", "false")
			 .formParam("address[enterManually]", "true")
        .formParam("hasCorrespondenceAddress", "false")
        .formParam("correspondenceAddress[postcodeLookup]", "")
        .formParam("correspondenceAddress[line1]", "")
        .formParam("correspondenceAddress[line2]", "")
        .formParam("correspondenceAddress[line3]", "")
        .formParam("correspondenceAddress[city]", "")
        .formParam("correspondenceAddress[postcode]", "")
        .formParam("correspondenceAddress[addressSelectorVisible]", "false")
			  .formParam("correspondenceAddress[enterManually]", "false")
        .formParam("saveAndContinue", "Save and continue")
        .check(CsrfCheck.save)
        .check(CurrentPageCheck.save)
        .check(regex("What is your date of birth")))
      .pause(thinktime)
      
      .exec(http("TX022_CMC_YourDetail_DateOfBirth-POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("known", "true")
        .formParam("date[day]", "31")
        .formParam("date[month]", "3")
        .formParam("date[year]", "1980")
        	.formParam("saveAndContinue", "Save and continue")
        .check(CsrfCheck.save)
        .check(CurrentPageCheck.save)
        .check(regex("Enter a phone number")))
      .pause(thinktime)
      
      .exec(http("TX023_CMC_YourDetail_AddPhoneNumber-POST")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("number", "07123456789")
        .formParam("saveAndContinue", "Save and continue")
        .check(regex("Their details")))
      .pause(thinktime)
  }

}

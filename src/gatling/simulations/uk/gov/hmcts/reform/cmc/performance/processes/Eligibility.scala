package uk.gov.hmcts.reform.cmc.performance.processes

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CsrfCheck.{csrfParameter, csrfTemplate}
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.CurrentPageCheck.currentPageTemplate
import uk.gov.hmcts.reform.cmc.performance.simulations.checks.{CsrfCheck, CurrentPageCheck}
import uk.gov.hmcts.reform.cmc.performance.utils._

object Eligibility {

  val thinktime = Environment.thinkTime

  def run(implicit postHeaders: Map[String, String]): ChainBuilder = {

      exec(http("TX03_CMC_Eligibility_TotalAmountYouAreclaiming_GET")
        .get("/eligibility/claim-value")
        .check(CsrfCheck.save)
        .check(regex("Total amount you’re claiming")))
        .exitHereIfFailed
        .pause(thinktime)

      .exec(http("TX04_CMC_Eligibility_TotalAmountYouAreclaiming_POST")
        .post("/eligibility/claim-value")
        .formParam(csrfParameter, csrfTemplate)
        .formParam("claimValue", "UNDER_10000")
        .check(CurrentPageCheck.save)
        .check(CsrfCheck.save)
        .check(regex("Is this claim against more than one person or organisation?")))
        .exitHereIfFailed
        .pause(thinktime)

        .exec(http("TX05_CMC_Eligibility_SingleDefendant")
          .post(currentPageTemplate)
          .formParam(csrfParameter, csrfTemplate)
          .formParam("singleDefendant", "no")
          .check(CurrentPageCheck.save)
          .check(CsrfCheck.save)
          .check(regex("Does the person or organisation you’re claiming against have a postal address in England or Wales?"))) //
        .exitHereIfFailed
        .pause(thinktime)

        .exec(http("TX06_CMC_Eligibility_DefendantAddress")
          .post(currentPageTemplate)
          .formParam(csrfParameter, csrfTemplate)
          .formParam("defendantAddress", "yes")
          .check(CurrentPageCheck.save)
          .check(CsrfCheck.save)
          .check(regex("Who are you making the claim for?")))
        .exitHereIfFailed//Are you 18 or over?
        .pause(thinktime)

        .exec(http("TX07_CMC_Eligibility_SingleClaimant")
          .post(currentPageTemplate)
          .formParam(csrfParameter, csrfTemplate)
          .formParam("claimType", "PERSONAL_CLAIM")
          .check(CurrentPageCheck.save)
          .check(CsrfCheck.save)
          .check(regex("Do you have a postal address in the UK?")))
        .exitHereIfFailed
        .pause(thinktime)

        .exec(http("TX08_CMC_Eligibility_ClaimantAddress")
       .post(currentPageTemplate)
       .formParam(csrfParameter, csrfTemplate)
       .formParam("claimantAddress", "yes")
       .check(CurrentPageCheck.save)
       .check(CsrfCheck.save)
       .check(regex("Is your claim for a tenancy deposit?")))
        .exitHereIfFailed
       .pause(thinktime)

        .exec(http("TX09_CMC_Eligibility_TenancyDeposit")
          .post("/eligibility/claim-is-for-tenancy-deposit")
          .formParam(csrfParameter, csrfTemplate)
          .formParam("claimIsForTenancyDeposit", "no")
          .check(CurrentPageCheck.save)
          .check(regex("Are you claiming against a government department?")))
        .exitHereIfFailed
        .pause(thinktime)

        .exec(http("TX010_CMC_Eligibility_GovernmentDepartment")
          .post(currentPageTemplate)
          .formParam(csrfParameter, csrfTemplate)
          .formParam("governmentDepartment", "no")
          .check(CurrentPageCheck.save)
          .check(CsrfCheck.save)
          .check(regex("Do you believe the person you’re claiming against is 18 or over?")))
        .exitHereIfFailed
        .pause(thinktime)

        .exec(http("TX011_CMC_Eligibility_DefendantAge")
          .post(currentPageTemplate)
          .formParam(csrfParameter, csrfTemplate)
          .formParam("defendantAge", "yes")
          .check(CurrentPageCheck.save)
          .check(CsrfCheck.save)
          .check(regex("Are you 18 or over?")))
        .exitHereIfFailed//Do you believe the person you’re claiming against is 18 or over?
        .pause(thinktime)

      .exec(http("TX012_CMC_Eligibility_Over18")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("eighteenOrOver", "yes")
        .check(CurrentPageCheck.save)
        //.check(CsrfCheck.save)
        .check(regex("Do you need help paying your court fees?")))
        .exitHereIfFailed//Do you believe the person you’re claiming against is 18 or over?
        .pause(thinktime)

      .exec(http("TX013_CMC_Eligibility_HelpWithCourtyFees")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("helpWithFees", "no")
        .check(CurrentPageCheck.save)
        .check(regex("You can use this service")))
        .pause(thinktime)

        //Who are you making the claim for?



     /* .exec(http("TX011_CMC_Eligibility_SingleClaimant")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("claimType", "PERSONAL_CLAIM")
        .check(CurrentPageCheck.save)
        .check(CsrfCheck.save)
        .check(regex("Is this claim against more than one person or organisation?")))
        .pause(thinktime)

      .exec(http("TX012_CMC_Eligibility_SingleDefendant")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("singleDefendant", "no")
        .check(CurrentPageCheck.save)
        .check(CsrfCheck.save)
        .check(regex("Are you claiming against a government department?")))
        .pause(thinktime)

      .exec(http("TX013_CMC_Eligibility_GovernmentDepartment")
        .post(currentPageTemplate)
        .formParam(csrfParameter, csrfTemplate)
        .formParam("governmentDepartment", "no")
        .check(CurrentPageCheck.save)
        .check(CsrfCheck.save)
        .check(regex("Is your claim for a tenancy deposit?")))
        .pause(thinktime)

      .exec(http("TX014_CMC_Eligibility_TenancyDeposit")
        .post("/eligibility/claim-is-for-tenancy-deposit")
        .formParam(csrfParameter, csrfTemplate)
        .formParam("claimIsForTenancyDeposit", "no")
        .check(CurrentPageCheck.save)
        .check(regex("You can use this service")))
        .pause(thinktime)*/

        .exec(http("TX014_CMC_New-Features-Consent")
          .post("/claim/new-features-consent")
          .formParam(csrfParameter, csrfTemplate)
          .formParam("consentResponse", "yes")
          .check(CurrentPageCheck.save))
        .exitHereIfFailed
        .pause(thinktime)


  }

}

package uk.gov.hmcts.reform.cmc.performance.processes

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.cmc.performance.utils._

object CheckAndSend {

	def run(implicit postHeaders: Map[String, String]): ChainBuilder = {

		val paymentURL = Environment.PaymentURL
		val thinktime = Environment.thinkTime
    val headers_996 = Map(
      "Accept" -> "*/*",
      "Accept-Encoding" -> "gzip, deflate, br",
      "Accept-Language" -> "en-US,en;q=0.9",
      "Content-type" -> "application/json",
      "Origin" -> "https://www.payments.service.gov.uk",
      "Sec-Fetch-Mode" -> "cors",
      "Sec-Fetch-Site" -> "same-origin",
      "User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")

		exec(http("TX036_CMC_CheckAndSend_Checkandsend_GET")
			.get("/claim/check-and-send")
			.check(css("input[name='_csrf']", "value").saveAs("_csrfCardDetailPage"))
			.check(regex("Check your answers")))
			.pause(thinktime)

			.exec(http("TX037_CMC_CheckAndSend_Checkandsend_POST")
				.post("/claim/check-and-send")
				.formParam("_csrf", "${_csrfCardDetailPage}")
				.formParam("signed", "true")
				.formParam("type", "basic")
				/*.check(regex("Claim submitted"))//this code for next 3 lines are added becuase payment is bypassed.
				.check(css(".receipt-download-container>a", "href").saveAs("pdfDownload"))
				.check(css(".reference-number>h1.bold-large").saveAs("claimNumber"))*/
				//below is the code with payments and later we need to add this code
					.check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailPage"))
          .check(css("input[name='chargeId']", "value").saveAs("_csrfCardDetailPageChargeId"))
          .check(regex("Enter card details"))
			)
			.pause(thinktime)

			.exec(http("TX038_CMC_CardDetail_CheckCardDetail")
				.post(paymentURL + "/check_card/${_csrfCardDetailPageChargeId}")
        .headers(headers_996)
			.body(RawFileBody("RecordedSimulationCMC0412latest_0996_request.txt")))
		  	.pause(thinktime)
				.exec(http("TX038_CMC_CardDetail_SubmitCardDetail")
          .post(paymentURL + "/card_details/${_csrfCardDetailPageChargeId}")
          .formParam("chargeId", "${_csrfCardDetailPageChargeId}")
          .formParam("csrfToken", "${_csrfTokenCardDetailPage}")
          .formParam("cardNo", "4444333322221111")
          .formParam("expiryMonth", "10")
          .formParam("expiryYear", "2021")
          .formParam("cardholderName", "Performance Test Credit Card")
          .formParam("cvc", "123")
          .formParam("addressCountry", "GB")
          .formParam("addressLine1", "123")
          .formParam("addressLine2", "")
          .formParam("addressCity", "London")
          .formParam("addressPostcode", "CF10 2HT")
          .formParam("email", "confirmation@confirmation.com")
          .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailConfirm"))
          .check(regex("Confirm your payment")))
        .pause(thinktime)

        .exec(http("TX039_CMC_CardDetail_ConfirmCardDetail")
          .post(paymentURL + "/card_details/${_csrfCardDetailPageChargeId}/confirm")
          .formParam("csrfToken", "${_csrfTokenCardDetailConfirm}")
          .formParam("chargeId", "${_csrfCardDetailPageChargeId}")
          .check(regex("Claim submitted"))
          .check(css(".receipt-download-container>a", "href").saveAs("pdfDownload"))
          .check(css(".reference-number>h1.bold-large").saveAs("claimNumber")))

        .pause(thinktime)

			.exec {
			session =>
				println("this is a pdf download url ....." + session("pdfDownload").as[String])
				println("claim number ....." + session("claimNumber").as[String])

				session
		}

			.exec(http("TX040_CMC_PDF_Download")
				.get("${pdfDownload}")
				.check(status.is(200)))
			.pause(thinktime)


	}
}

/*	.exec(http("TX040_CMC_ClaimSubmitted_getPDF_GET")
  .get("/claim/${_csrfCardDetailPageChargeId}/receipt")
  )
  .pause(thinktime)*/

//https://www.moneyclaims.demo.platform.hmcts.net/claim/e360df48-6ca0-48bd-aa69-c5ae7d0ad452/receipt
/*	.exec(http("TX050_CMC_Logout")
    .get("/logout")
    .check(regex("Sign in")))
  .pause(thinktime) */




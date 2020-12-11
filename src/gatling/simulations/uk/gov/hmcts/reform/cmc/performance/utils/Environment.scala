package uk.gov.hmcts.reform.cmc.performance.utils

object Environment {
  val httpConfig = scala.util.Properties.envOrElse("httpConfig", "http")

 //val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://cmc-citizen-frontend-perftest.service.core-compute-perftest.internal")
 val cmcLegalURL = scala.util.Properties.envOrElse("cmcLegalURL", "https://moneyclaims-legal.demo.platform.hmcts.net/legal")
 //val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://cmc-citizen-frontend-aat.service.core-compute-aat.internal")
 val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://moneyclaims.aat.platform.hmcts.net")
 //val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://moneyclaims.demo.platform.hmcts.net")
 // val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://moneyclaim.nonprod.platform.hmcts.net")
  //val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://www-demo.moneyclaim.reform.hmcts.net")
  //val cmcBashURL = scala.util.Properties.envOrElse("baseURL", "https://moneyclaim.nonprod.platform.hmcts.net")
  //val iadmURL = "https://idam-test.dev.ccidam.reform.hmcts.net"
  val PaymentURL = scala.util.Properties.envOrElse("paymentURL", "https://www.payments.service.gov.uk")
  //val idamBaseURL=scala.util.Properties.envOrElse("idamBaseURL", "https://idam-api-idam-perftest.service.core-compute-idam-perftest.internal")
  //val idamBaseURL=scala.util.Properties.envOrElse("idamBaseURL", "https://idam-api.perftest.platform.hmcts.net")
  //val idamBaseURL=scala.util.Properties.envOrElse("idamBaseURL", "https://idam-web-public.demo.platform.hmcts.net")
  //val idamBaseURL=scala.util.Properties.envOrElse("idamBaseURL", "https://idam-api.demo.platform.hmcts.net")
 //val idamBaseURL=scala.util.Properties.envOrElse("idamBaseURL", "https://idam-api-idam-aat.service.core-compute-idam-aat.internal")
  val idamBaseURL=scala.util.Properties.envOrElse("idamBaseURL", "https://idam-web-public.aat.platform.hmcts.net")
  val users = scala.util.Properties.envOrElse("numberOfUser", "1").toInt
  val maxResponseTime = scala.util.Properties.envOrElse("maxResponseTime", "500")
  val idamCookieName="SESSION_ID"

  val commonHeader = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-GB,en-US;q=0.9,en;q=0.8",
		"Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")

  val thinkTime = scala.util.Properties.envOrElse("thinkTime", "2").toInt
  val waitForNextIteration = 80
}

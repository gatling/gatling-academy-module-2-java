package gatlingdemostore;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;
import java.util.*;

public class DemostoreSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol =
        http.baseUrl("https://demostore.gatling.io")
            .inferHtmlResources(
                AllowList(),
                DenyList(
                    ".*\\.js",
                    ".*\\.css",
                    ".*\\.gif",
                    ".*\\.jpeg",
                    ".*\\.jpg",
                    ".*\\.ico",
                    ".*\\.woff",
                    ".*\\.woff2",
                    ".*\\.(t|o)tf",
                    ".*\\.png",
                    ".*detectportal\\.firefox\\.com.*"))
            .acceptHeader(
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-US,en;q=0.9")
            .userAgentHeader(
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36");

    Map<CharSequence, String> headers_0 = new HashMap<>();
    headers_0.put("Cache-Control", "max-age=0");
    headers_0.put("Sec-Fetch-Dest", "document");
    headers_0.put("Sec-Fetch-Mode", "navigate");
    headers_0.put("Sec-Fetch-Site", "none");
    headers_0.put("Sec-Fetch-User", "?1");
    headers_0.put("Upgrade-Insecure-Requests", "1");
    headers_0.put(
        "sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98");
    headers_0.put("sec-ch-ua-mobile", "?0");
    headers_0.put("sec-ch-ua-platform", "Linux");

    Map<CharSequence, String> headers_1 = new HashMap<>();
    headers_1.put("Sec-Fetch-Dest", "document");
    headers_1.put("Sec-Fetch-Mode", "navigate");
    headers_1.put("Sec-Fetch-Site", "same-origin");
    headers_1.put("Sec-Fetch-User", "?1");
    headers_1.put("Upgrade-Insecure-Requests", "1");
    headers_1.put(
        "sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98");
    headers_1.put("sec-ch-ua-mobile", "?0");
    headers_1.put("sec-ch-ua-platform", "Linux");

    Map<CharSequence, String> headers_4 = new HashMap<>();
    headers_4.put("Accept", "*/*");
    headers_4.put("Sec-Fetch-Dest", "empty");
    headers_4.put("Sec-Fetch-Mode", "cors");
    headers_4.put("Sec-Fetch-Site", "same-origin");
    headers_4.put("X-Requested-With", "XMLHttpRequest");
    headers_4.put(
        "sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"98\", \"Google Chrome\";v=\"98");
    headers_4.put("sec-ch-ua-mobile", "?0");
    headers_4.put("sec-ch-ua-platform", "Linux");

    Map<CharSequence, String> headers_6 = new HashMap<>();
    headers_6.put("Accept-Encoding", "gzip, deflate");
    headers_6.put("Cache-Control", "max-age=0");
    headers_6.put("Origin", "http://demostore.gatling.io");
    headers_6.put("Upgrade-Insecure-Requests", "1");

    Map<CharSequence, String> headers_7 = new HashMap<>();
    headers_7.put("Accept-Encoding", "gzip, deflate");
    headers_7.put("Upgrade-Insecure-Requests", "1");

    String uri1 = "demostore.gatling.io";

    ScenarioBuilder scn =
        scenario("DemostoreSimulation")
            .exec(http("request_0").get("/").headers(headers_0))
            .pause(8)
            .exec(http("request_1").get("/about-us").headers(headers_1))
            .pause(2)
            .exec(http("request_2").get("/category/all").headers(headers_1))
            .pause(6)
            .exec(http("request_3").get("/product/black-and-red-glasses").headers(headers_1))
            .pause(7)
            .exec(http("request_4").get("/cart/add/19").headers(headers_4))
            .pause(2)
            .exec(http("request_5").get("/cart/view").headers(headers_1))
            .pause(9)
            .exec(
                http("request_6")
                    .post("http://" + uri1 + "/login")
                    .headers(headers_6)
                    .formParam("_csrf", "7257dcaf-d850-48f2-a2ff-e8f5e316405b")
                    .formParam("username", "user1")
                    .formParam("password", "pass"))
            .pause(7)
            .exec(http("request_7").get("http://" + uri1 + "/cart/checkout").headers(headers_7));

    setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}

package gatlingdemostore;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class DemostoreSimulation extends Simulation {

  private static final String DOMAIN = "demostore.gatling.io";
  private static final HttpProtocolBuilder HTTP_PROTOCOL = http.baseUrl("https://" + DOMAIN);

  private static final ScenarioBuilder scn =
      scenario("DemostoreSimulation")
          .exec(
              http("Load Home Page")
                  .get("/")
                  .check(regex("<title>Gatling Demo-Store</title>").exists())
                  .check(css("#_csrf", "content").saveAs("csrfValue")))
          .pause(2)
          .exec(http("Load About Us Page").get("/about-us"))
          .pause(2)
          .exec(http("Load Categories Page").get("/category/all"))
          .pause(2)
          .exec(http("Load Product Page").get("/product/black-and-red-glasses"))
          .pause(2)
          .exec(http("Add Product to Cart").get("/cart/add/19"))
          .pause(2)
          .exec(http("View Cart").get("/cart/view"))
          .pause(2)
          .exec(
              http("Login User")
                  .post("/login")
                  .formParam("_csrf", "${csrfValue}")
                  .formParam("username", "user1")
                  .formParam("password", "pass"))
          .pause(2)
          .exec(http("Checkout").get("/cart/checkout"));

  {
    setUp(scn.injectOpen(atOnceUsers(1))).protocols(HTTP_PROTOCOL);
  }
}

package gatlingdemostore;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.util.concurrent.ThreadLocalRandom;

public class DemostoreSimulation extends Simulation {

  private static final String DOMAIN = "demostore.gatling.io";
  private static final HttpProtocolBuilder HTTP_PROTOCOL = http.baseUrl("https://" + DOMAIN);

  private static final FeederBuilder<String> categoryFeeder =
      csv("data/categoryDetails.csv").random();

  private static final FeederBuilder<Object> jsonFeederProducts =
      jsonFile("data/productDetails.csv").random();

  private static final FeederBuilder<Object> csvFeederLoginDetails =
      jsonFile("data/loginDetails.csv").circular();

  private static final ChainBuilder initSession =
      exec(flushCookieJar())
          .exec(session -> session.set("randomNumber", ThreadLocalRandom.current().nextInt()))
          .exec(session -> session.set("customerLoggedIn", false))
          .exec(session -> session.set("cartTotal", 0.00))
          .exec(addCookie(Cookie("sessionId", SessionId.random()).withDomain(DOMAIN)))
          .exec(
              session -> {
                System.out.println(session.toString());
                return session;
              });

  private static class CmsPages {

    private static final ChainBuilder homepage =
        exec(
            http("Load Home Page")
                .get("/")
                .check(regex("<title>Gatling Demo-Store</title>").exists())
                .check(css("#_csrf", "content").saveAs("csrfValue")));

    private static final ChainBuilder aboutUs =
        exec(http("Load About Us Page").get("/about-us").check(substring("About Us")));
  }

  private static class Catalog {
    private static class Category {
      private static final ChainBuilder view =
          feed(categoryFeeder)
              .exec(
                  http("Load Category Page - #{categoryName}")
                      .get("/category/#{categorySlug}")
                      .check(css("#CategoryName").is("#{categoryName}")));
    }

    private static class Product {
      private static final ChainBuilder view =
          feed(jsonFeederProducts)
              .exec(
                  http("Load Product Page - #{name}")
                      .get("/product/#{slug}")
                      .check(css("#ProductDescription").is("#{description}")));

      private static final ChainBuilder add =
          exec(view)
              .exec(
                  http("Add Product to Cart")
                      .get("/cart/add/#{id}")
                      .check(substring("items in your cart")));
    }
  }

  private static class Customer {
    private static final ChainBuilder login =
        feed(csvFeederLoginDetails)
            .exec(http("Load Login Page").get("/login").check(substring("Username:")))
            .exec(
                http("Customer Login Action")
                    .post("/login")
                    .formParam("_csrf", "#{csrfValue}")
                    .formParam("username", "#{username}")
                    .formParam("password", "#{password}"));
  }

  private static class Checkout {
    private static final ChainBuilder viewCart = exec(http("Load Cart Page").get("/cart/view"));

    private static final ChainBuilder completeCheckout =
        exec(
            http("Checkout Cart")
                .get("/cart/checkout")
                .check(substring("Thanks for your order! See you soon!")));
  }

  private static final ScenarioBuilder scn =
      scenario("DemostoreSimulation")
          .exec(initSession)
          .exec(CmsPages.homepage)
          .pause(2)
          .exec(CmsPages.aboutUs)
          .pause(2)
          .exec(Catalog.Category.view)
          .pause(2)
          .exec(Catalog.Product.add)
          .pause(2)
          .exec(Checkout.viewCart)
          .pause(2)
          .exec(Customer.login)
          .pause(2)
          .exec(Checkout.completeCheckout);

  {
    setUp(scn.injectOpen(atOnceUsers(1))).protocols(HTTP_PROTOCOL);
  }
}

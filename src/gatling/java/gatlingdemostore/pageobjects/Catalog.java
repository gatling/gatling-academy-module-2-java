package gatlingdemostore.pageobjects;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;

public final class Catalog {

  private static final FeederBuilder<String> categoryFeeder =
      csv("data/categoryDetails.csv").random();

  private static final FeederBuilder<Object> jsonFeederProducts =
      jsonFile("data/productDetails.json").random();

  public static final class Category {
    public static ChainBuilder view =
        feed(categoryFeeder)
            .exec(
                http("Load Product Page - #{categoryName}")
                    .get("/category/#{categorySlug}")
                    .check(css("h2[id='CategoryName']").isEL("#{categoryName}")));
  }

  public static class Product {
    public static ChainBuilder view =
        feed(jsonFeederProducts)
            .exec(
                http("Load Product Page - #{name}")
                    .get("/product/#{slug}")
                    .check(css("div[class='col-8'] div[class='row'] p").isEL("#{description}")));

    public static ChainBuilder add =
        exec(view)
            .exec(
                http("Add product to cart")
                    .get("/cart/add/#{id}")
                    .check(regex("items in your cart")))
            .exec(
                session -> {
                  Double currentCartTotal = session.getDouble("cartTotal");
                  Double itemPrice = session.getDouble("price");
                  return session.set("cartTotal", currentCartTotal + itemPrice);
                });
  }
}

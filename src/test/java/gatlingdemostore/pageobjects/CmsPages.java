package gatlingdemostore.pageobjects;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;

public class CmsPages {

  public static ChainBuilder homepage =
      exec(
          http("Load Home Page")
              .get("/")
              .check(regex("<title>Gatling Demo-Store</title>").exists())
              .check(css("#_csrf", "content").saveAs("csrfValue")));

  public static ChainBuilder aboutUs =
      exec(
          http("Load About Us Page")
              .get("/about-us")
              .check(css("div[class='col-7'] h2").is("About Us")));
}

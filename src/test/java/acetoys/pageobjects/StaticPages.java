package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class StaticPages {
    public static ChainBuilder homePage = exec(
            http("Load Home Page")
                    .get("/")
                    .check(status().is(200)) //gatling does this check by default
                    .check(status().not(404), status().not(405))
                    .check(substring("<title>Ace Toys Online Shop</title>")) //gatling checks the response html
                    .check(css("#_csrf", "content").saveAs("csrfToken")) //gatling finds an element on the page
    );
    public static ChainBuilder ourStoryPage = exec(
            http("Load our story page")
                    .get("/our-story")
                    .check(regex("was founded online in \\d{4}")) //gatling regex on html page
    );

    public static ChainBuilder getInTouchPage = exec(
            http("Get in touch page")
                    .get("/get-in-touch")
                    .check(substring("as we are not actually a real store!"))
    );
}

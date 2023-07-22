package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class CustomerPages {
    /* loginFeeder is just a demonstration of custom feeder code. Recommendation for login credentials would be to use a CSV file */
    private static Iterator<Map<String, Object>> loginFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random random = new Random();
                int userId = random.nextInt(3 - 1 + 1) + 1;
                HashMap<String, Object> hmap = new HashMap<>();
                hmap.put("userId", "user" + userId);
                hmap.put("password", "pass");
                return hmap;
            }).iterator();
    public static ChainBuilder login =
            feed(loginFeeder).exec(
                            http("Login User")
                                    .post("/login")
                                    .formParam("_csrf", "#{csrfToken}")
                                    .formParam("username", "#{userId}")
                                    .formParam("password", "#{password}")
                                    .check(css("#_csrf", "content").saveAs("csrfTokenLoggedIn")))
                    /* for demonstration purposes printing out generated userId */
                    .exec(session -> {
                        System.out.println(session.getString("userId"));
                        return session;
                    })
                    .exec(session -> session.set("customerLoggedIn", true));
    // only 10% of users click on logout button after finishing the purchase
    public static ChainBuilder logout =
            randomSwitch().on(
                    Choice.withWeight(10, exec(
                            http("Logout")
                                    .post("/logout")
                                    .formParam("_csrf", "#{csrfTokenLoggedIn}")
                                    .check(css("#LoginLink").is("Login"))
                    )));
}
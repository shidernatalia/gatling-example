package acetoys;

import acetoys.simulation.TestPopulation;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class AceToysSimulation extends Simulation {
    private static final String TEST_TYPE = System.getenv("TEST_TYPE");
    private static final String DOMAIN = "acetoys.uk";

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://" + DOMAIN)
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,el;q=0.6,de;q=0.5,ro;q=0.4,es;q=0.3");

    {
        switch (TEST_TYPE) {
            case "RAMP_USERS" -> setUp(TestPopulation.rampedUsers).protocols(httpProtocol)
                    .assertions(
                            global().responseTime().mean().lt(10),
                            global().successfulRequests().percent().gt(99.0),
                            forAll().responseTime().max().lt(5));
            case "COMPLEX_INJECTION" -> setUp(TestPopulation.complexInjection).protocols(httpProtocol)
                    .assertions(
                            global().responseTime().mean().lt(10),
                            global().successfulRequests().percent().gt(99.0),
                            forAll().responseTime().max().lt(5));
            case "CLOSED_MODEL" -> setUp(TestPopulation.closedModel).protocols(httpProtocol)
                    .assertions(
                            global().responseTime().mean().lt(10),
                            global().successfulRequests().percent().gt(99.0),
                            forAll().responseTime().max().lt(5));
            default -> setUp(TestPopulation.instantUsers).protocols(httpProtocol)
                    .assertions(
                            global().responseTime().mean().lt(10),
                            global().successfulRequests().percent().gt(99.0),
                            forAll().responseTime().max().lt(5));
        }
    }
}

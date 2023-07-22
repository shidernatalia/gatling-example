package acetoys.simulation;


import io.gatling.javaapi.core.PopulationBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.core.OpenInjectionStep.nothingFor;

public class TestPopulation {
    private static final int USER_COUNT = Integer.parseInt(System.getenv("USERS"));
    private static final Duration RAMP_DURATION = Duration.ofSeconds(Integer.parseInt(System.getenv("RAMP_DURATION")));

    public static PopulationBuilder instantUsers =
            TestScenario.deaultLoadTest
                    .injectOpen(
                            nothingFor(Duration.ofMillis(5000)),
                            atOnceUsers(USER_COUNT));

    public static PopulationBuilder rampedUsers =
            TestScenario.deaultLoadTest
                    .injectOpen(
                            nothingFor(Duration.ofMillis(5000)),
                            rampUsers(USER_COUNT).during(RAMP_DURATION));

    public static PopulationBuilder complexInjection =
            TestScenario.deaultLoadTest
                    .injectOpen(
                            constantUsersPerSec(3).during(10).randomized(),
                            rampUsersPerSec(3).to(5).during(10).randomized()
                    );

    public static PopulationBuilder closedModel =
            TestScenario.highPurchaseLoadTest
                    .injectClosed(
                            constantConcurrentUsers(3).during(10),
                            rampConcurrentUsers(3).to(5).during(10)
                    );

}

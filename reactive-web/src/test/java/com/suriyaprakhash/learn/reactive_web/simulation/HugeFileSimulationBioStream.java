package com.suriyaprakhash.learn.reactive_web.simulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class HugeFileSimulationBioStream extends Simulation {

    // Http Configuration
    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("http://localhost:8080/");

    // Scenario Definition
    private ChainBuilder hugeFileBioStream =
            exec(http("Download file as bio stream")
            .get("hugefile/bio/stream"));


    private ScenarioBuilder loadTestUser = scenario("load test user").exec(hugeFileBioStream);

    // Load simulation
    {
        setUp(
                loadTestUser.injectOpen(rampUsers(60).during(Duration.ofSeconds(30)))
        ).protocols(httpProtocolBuilder);
    }
}

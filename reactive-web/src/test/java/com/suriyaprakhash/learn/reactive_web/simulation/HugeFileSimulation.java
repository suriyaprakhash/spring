package com.suriyaprakhash.learn.reactive_web.simulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class HugeFileSimulation extends Simulation {

    // Http Configuration
    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("http://localhost:8081/hugefile");

    // Scenario Definition
    private ScenarioBuilder hugeFileBioStreamScenario = scenario("huge file bio stream")
            .exec(http("Download file as bio stream")
            .get("/bio/stream"));

    // Load simulation
    {
        setUp(
                hugeFileBioStreamScenario.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocolBuilder);
    }
}

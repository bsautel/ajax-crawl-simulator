package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static fr.fierdecoder.ajaxcrawlsimulator.web.application.WebServiceTestRule.*;
import static net.codestory.http.constants.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class SimulationsResourceTest {
    @Rule
    public WebServiceTestRule webServiceRule = new WebServiceTestRule();

    @Test
    public void shouldReturnNoSimulationWhenStartingFromScratch() {
        webServiceRule.restClient().get(SIMULATIONS_PATH).then()
                .statusCode(OK)
                .body("size()", is(0));
    }

    @Test
    public void shouldReturnTheSimulationWhenCreatingASimulation() throws IOException {
        webServiceRule.createSimulation().then()
                .statusCode(CREATED)
                .body("name", is(SIMULATION_NAME))
                .body("entryUrl", is(URL))
                .body("urlPrefix", is(URL));
    }

    @Test
    public void shouldReturnASimulationWhenASimulationWasCreated() throws IOException {
        webServiceRule.createSimulation();

        webServiceRule.restClient().get(SIMULATIONS_PATH).then()
                .statusCode(OK)
                .body("name", containsInAnyOrder(SIMULATION_NAME))
                .body("entryUrl", hasItems(URL))
                .body("urlPrefix", hasItems(URL));
    }

    @Test
    public void shouldFailWhenCreatingASimulationThatAlreadyExists() throws IOException {
        webServiceRule.createSimulation();

        webServiceRule.createSimulation().then()
                .statusCode(CONFLICT);
    }
}

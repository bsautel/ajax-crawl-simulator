package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import org.junit.Test;

import java.io.IOException;

import static net.codestory.http.constants.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class SimulationsResourceTest extends AbstractWebServiceTest {
    @Test
    public void shouldReturnNoSimulationWhenStartingFromScratch() {
        restClient().get(SIMULATIONS_PATH).then()
                .statusCode(OK)
                .body("size()", is(0));
    }

    @Test
    public void shouldReturnTheSimulationWhenCreatingASimulation() throws IOException {
        createSimulation().then()
                .statusCode(CREATED)
                .body("name", is(SIMULATION_NAME))
                .body("entryUrl", is(URL))
                .body("urlPrefix", is(URL));
    }

    @Test
    public void shouldReturnASimulationWhenASimulationWasCreated() throws IOException {
        createSimulation();

        restClient().get(SIMULATIONS_PATH).then()
                .statusCode(OK)
                .body("name", containsInAnyOrder(SIMULATION_NAME))
                .body("entryUrl", hasItems(URL))
                .body("urlPrefix", hasItems(URL));
    }

    @Test
    public void shouldFailWhenCreatingASimulationThatAlreadyExists() throws Exception {
        createSimulation();

        createSimulation().then()
                .statusCode(CONFLICT);
    }
}

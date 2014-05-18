package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import org.junit.Test;

import java.io.IOException;

import static fr.fierdecoder.ajaxcrawlsimulator.web.application.CrawlerStub.*;
import static java.net.URLEncoder.encode;
import static org.hamcrest.Matchers.*;

public class SimulationResourceTest extends AbstractWebServiceTest {
    private static final String SIMULATION_PAGES_PATH = SIMULATION_PATH + "/pages";

    @Test
    public void shouldRetrieveAnExistingSimulation() throws IOException {
        createSimulation();

        restClient().get(SIMULATION_PATH).then()
                .statusCode(200)
                .body("name", is(SIMULATION_NAME))
                .body("entryUrl", is(URL))
                .body("urlPrefix", is(URL));
    }

    @Test
    public void shouldDeleteAnExistingSimulation() throws IOException {
        createSimulation();

        restClient().delete(SIMULATION_PATH).then()
                .statusCode(200);

        restClient().get(SIMULATIONS_PATH).then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void shouldReturnSimulationPages() throws IOException {
        createSimulation();

        restClient().get(SIMULATION_PAGES_PATH).then()
                .statusCode(200)
                .body("size()", is(3))
                .body("url", containsInAnyOrder(ABOUT_URL, CONTACT_URL, HOME_URL))
                .body(findByUrlExpression(ABOUT_URL) + ".type", is("HTML"))
                .body(findByUrlExpression(ABOUT_URL) + ".title", is(PAGE_TITLE))
                .body(findByUrlExpression(CONTACT_URL) + ".type", is("UNREACHABLE"))
                .body(findByUrlExpression(CONTACT_URL) + ".title", isEmptyOrNullString())
                .body(findByUrlExpression(HOME_URL) + ".type", is("REDIRECTION"))
                .body(findByUrlExpression(HOME_URL) + ".title", isEmptyOrNullString());
    }

    @Test
    public void shouldReturnPage() throws IOException {
        createSimulation();

        String aboutPagePath = SIMULATION_PAGES_PATH + "/" + encode(ABOUT_URL, "utf-8");
        restClient().get(aboutPagePath).then()
                .statusCode(200)
                .body("url", is(ABOUT_URL))
                .body("title", is(PAGE_TITLE))
                .body("body", is(PAGE_BODY))
                .body("httpStatus", is(200))
                .body("type", is("HTML"));
    }

    private String findByUrlExpression(String url) {
        return "find{it.url == '" + url + "'}";
    }
}

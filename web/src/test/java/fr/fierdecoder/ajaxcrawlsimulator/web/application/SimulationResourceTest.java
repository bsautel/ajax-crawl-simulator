package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static fr.fierdecoder.ajaxcrawlsimulator.web.application.CrawlerStub.*;
import static java.net.URLEncoder.encode;
import static net.codestory.http.constants.HttpStatus.OK;
import static org.hamcrest.Matchers.*;

public class SimulationResourceTest extends AbstractWebServiceTest {
    private static final String SIMULATION_PAGES_PATH = SIMULATION_PATH + "/pages";

    @Test
    public void shouldRetrieveAnExistingSimulationWhenASimulationExists() throws IOException {
        createSimulation();

        restClient().get(SIMULATION_PATH).then()
                .statusCode(OK)
                .body("name", is(SIMULATION_NAME))
                .body("entryUrl", is(URL))
                .body("urlPrefix", is(URL));
    }

    @Test
    public void shouldNotExistAnySimulationWhenTheExistingOneIsDeleted() throws IOException {
        createSimulation();

        restClient().delete(SIMULATION_PATH).then()
                .statusCode(OK);

        restClient().get(SIMULATIONS_PATH).then()
                .statusCode(OK)
                .body("size()", is(0));
    }

    @Test
    public void shouldReturnSimulationPagesWhenASimulationExists() throws IOException {
        createSimulation();

        restClient().get(SIMULATION_PAGES_PATH).then()
                .statusCode(OK)
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
    public void shouldReturnHtmlPageWhenAskingTheAboutPage() throws IOException {
        createSimulation();

        String aboutPagePath = computePagePath(ABOUT_URL);
        restClient().get(aboutPagePath).then()
                .statusCode(OK)
                .body("url", is(ABOUT_URL))
                .body("title", is(PAGE_TITLE))
                .body("body", is(PAGE_BODY))
                .body("httpStatus", is(200))
                .body("type", is("HTML"));
    }

    @Test
    public void shouldReturnRedirectionPageWhenAskingTheHomePage() throws IOException {
        createSimulation();

        String aboutPagePath = computePagePath(HOME_URL);
        restClient().get(aboutPagePath).then()
                .statusCode(OK)
                .body("url", is(HOME_URL))
                .body("targetUrl", is(CONTACT_URL))
                .body("httpStatus", is(301))
                .body("type", is("REDIRECTION"));
    }

    private String computePagePath(String aboutUrl) throws UnsupportedEncodingException {
        return SIMULATION_PAGES_PATH + "/" + encode(aboutUrl, "utf-8");
    }
    private String findByUrlExpression(String url) {
        return "find{it.url == '" + url + "'}";
    }
}

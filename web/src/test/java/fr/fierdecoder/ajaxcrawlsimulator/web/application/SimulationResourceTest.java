package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static fr.fierdecoder.ajaxcrawlsimulator.web.application.CrawlerStub.*;
import static fr.fierdecoder.ajaxcrawlsimulator.web.application.WebServiceTestRule.*;
import static net.codestory.http.constants.HttpStatus.OK;
import static org.hamcrest.Matchers.*;

public class SimulationResourceTest {
    private static final String SIMULATION_PAGES_PATH = SIMULATION_PATH + "/pages";
    @Rule
    public WebServiceTestRule webServiceRule = new WebServiceTestRule();

    @Test
    public void shouldRetrieveAnExistingSimulationWhenASimulationExists() throws IOException {
        webServiceRule.createAndReturnSimulation();

        webServiceRule.restClient().get(SIMULATION_PATH).then()
                .statusCode(OK)
                .body("name", is(SIMULATION_NAME))
                .body("entryUrl", is(URL))
                .body("urlPrefix", is(URL));
    }

    @Test
    public void shouldNotExistAnySimulationWhenTheExistingOneIsDeleted() throws IOException {
        webServiceRule.createAndReturnSimulation();

        webServiceRule.restClient().delete(SIMULATION_PATH).then()
                .statusCode(OK);

        webServiceRule.restClient().get(SIMULATIONS_PATH).then()
                .statusCode(OK)
                .body("size()", is(0));
    }

    @Test
    public void shouldReturnSimulationPagesWhenASimulationExists() throws IOException {
        Simulation simulation = webServiceRule.createAndReturnSimulation();

        webServiceRule.restClient().get(SIMULATION_PAGES_PATH).then()
                .statusCode(OK)
                .log().body()
                .body("size()", is(3))
                .body("url", containsInAnyOrder(ABOUT_URL, CONTACT_URL, HOME_URL))
                .body("id", containsInAnyOrder(computePageId(simulation, ABOUT_URL), computePageId(simulation, CONTACT_URL), computePageId(simulation, HOME_URL)))
                .body(findByUrlExpression(ABOUT_URL) + ".type", is("HTML"))
                .body(findByUrlExpression(ABOUT_URL) + ".title", is(PAGE_TITLE))
                .body(findByUrlExpression(CONTACT_URL) + ".type", is("UNREACHABLE"))
                .body(findByUrlExpression(CONTACT_URL) + ".title", isEmptyOrNullString())
                .body(findByUrlExpression(HOME_URL) + ".type", is("REDIRECTION"))
                .body(findByUrlExpression(HOME_URL) + ".title", isEmptyOrNullString());
    }

    @Test
    public void shouldReturnHtmlPageWhenAskingTheAboutPage() throws IOException {
        Simulation simulation = webServiceRule.createAndReturnSimulation();

        String aboutPagePath = computePagePath(simulation, ABOUT_URL);
        webServiceRule.restClient().get(aboutPagePath).then()
                .statusCode(OK)
                .body("url", is(ABOUT_URL))
                .body("title", is(PAGE_TITLE))
                .body("body", is(PAGE_BODY))
                .body("httpStatus", is(200))
                .body("type", is("HTML"));
    }

    @Test
    public void shouldReturnRedirectionPageWhenAskingTheHomePage() throws IOException {
        Simulation simulation = webServiceRule.createAndReturnSimulation();

        String aboutPagePath = computePagePath(simulation, HOME_URL);
        webServiceRule.restClient().get(aboutPagePath).then()
                .statusCode(OK)
                .body("url", is(HOME_URL))
                .body("targetUrl", is(CONTACT_URL))
                .body("httpStatus", is(301))
                .body("type", is("REDIRECTION"));
    }

    private String computePagePath(Simulation simulation, String url) {
        String id = computePageId(simulation, url);
        return SIMULATION_PAGES_PATH + "/" + id;
    }

    private String computePageId(Simulation simulation, String url) {
        Optional<WebPage> pageByUrl = simulation.getState().getPageByUrl(url);
        WebPage webPage = pageByUrl.get();
        return webPage.getId();
    }

    private String findByUrlExpression(String url) {
        return "find{it.url == '" + url + "'}";
    }
}

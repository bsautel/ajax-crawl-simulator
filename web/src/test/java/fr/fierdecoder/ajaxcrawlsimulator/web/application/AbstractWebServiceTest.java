package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.common.io.CharStreams;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

public class AbstractWebServiceTest {
    public static final String URL = "http://abc.d/";
    public static final String SIMULATION_NAME = "simu";
    public static final String SIMULATIONS_PATH = "/simulations";
    public static final String SIMULATION_PATH = SIMULATIONS_PATH + "/" + SIMULATION_NAME;
    private WebApplication application;

    @Before
    public void setUp() throws Exception {
        TestModule testModule = new TestModule();
        application = new WebApplication(testModule);
        application.startOnRandomPort();
    }

    protected RequestSpecification restClient() {
        return given().port(application.port());
    }

    @After
    public void tearDown() throws Exception {
        application.stop();
    }

    protected Response createSimulation() throws IOException {
        return restClient().body(getSimulationJson()).contentType(JSON).post(SIMULATIONS_PATH);
    }

    private String getSimulationJson() throws IOException {
        InputStream stream = getClass().getResourceAsStream("simulation.json");
        return CharStreams.toString(new InputStreamReader(stream, "utf-8"));
    }
}

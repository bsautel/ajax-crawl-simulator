package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.common.io.CharStreams;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

public class WebServiceTestRule extends ExternalResource {
    public static final String URL = "http://abc.d/";
    public static final String SIMULATION_NAME = "simu";
    public static final String SIMULATIONS_PATH = "/simulations";
    public static final String SIMULATION_PATH = SIMULATIONS_PATH + "/" + SIMULATION_NAME;
    private WebApplication application;


    public RequestSpecification restClient() {
        return given().port(application.port());
    }


    public Response createSimulation() throws IOException {
        return restClient().body(getSimulationJson()).contentType(JSON).post(SIMULATIONS_PATH);
    }

    public String getSimulationJson() throws IOException {
        InputStream stream = getClass().getResourceAsStream("simulation.json");
        return CharStreams.toString(new InputStreamReader(stream, "utf-8"));
    }

    @Override
    protected void before() throws Throwable {
        TestModule testModule = new TestModule();
        application = new WebApplication(testModule);
        application.startOnRandomPort();
    }

    @Override
    protected void after() {
        application.stop();
    }
}

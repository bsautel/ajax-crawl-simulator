package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import org.junit.rules.ExternalResource;

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


    public Response createSimulation() throws JsonProcessingException {
        return restClient().body(getSimulationJson()).contentType(JSON).post(SIMULATIONS_PATH);
    }

    private String getSimulationJson() throws JsonProcessingException {
        SimulationDescriptor simulation = SimulationDescriptor.create("simu", "http://abc.d/", "http://abc.d/");
        return new ObjectMapper().writer().writeValueAsString(simulation);
    }

    @Override
    protected void before() {
        TestModule testModule = new TestModule();
        application = new WebApplication(testModule);
        application.startOnRandomPort();
    }

    @Override
    protected void after() {
        application.stop();
    }
}

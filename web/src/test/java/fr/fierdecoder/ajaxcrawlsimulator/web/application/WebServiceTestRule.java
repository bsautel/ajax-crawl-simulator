package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.MemoryCrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.MemorySimulationsRepository;
import org.junit.rules.ExternalResource;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

public class WebServiceTestRule extends ExternalResource {
    public static final String URL = "http://abc.d/";
    public static final String SIMULATION_NAME = "simu";
    public static final String SIMULATIONS_PATH = "/simulations";
    public static final String SIMULATION_PATH = SIMULATIONS_PATH + "/" + SIMULATION_NAME;
    private WebApplication application;
    private MemorySimulationsRepository simulationsRepository;

    public RequestSpecification restClient() {
        return given().port(application.port());
    }

    public Simulation createAndReturnSimulation() throws JsonProcessingException {
        createSimulationAndReturnHttpResponse();
        return simulationsRepository.get(SIMULATION_NAME).get();
    }

    public Response createSimulationAndReturnHttpResponse() throws JsonProcessingException {
        return restClient().body(aSimulationDescriptionJson())
                .contentType(JSON).post(SIMULATIONS_PATH);
    }

    private String aSimulationDescriptionJson() throws JsonProcessingException {
        SimulationDescriptor simulation = aSimulationDescription();
        return new ObjectMapper().writer().writeValueAsString(simulation);
    }

    private SimulationDescriptor aSimulationDescription() {
        return SimulationDescriptor.create("simu", "http://abc.d/", "http://abc.d/");
    }

    @Override
    protected void before() {
        TestModule testModule = new TestModule();
        simulationsRepository = testModule.getSimulationsRepository();
        application = new WebApplication(testModule);
        application.startOnRandomPort();
    }

    @Override
    protected void after() {
        application.stop();
    }
}

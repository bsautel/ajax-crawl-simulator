package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Module;
import fr.fierdecoder.ajaxcrawlsimulator.web.resource.SimulationResource;
import fr.fierdecoder.ajaxcrawlsimulator.web.resource.SimulationsResource;
import net.codestory.http.WebServer;
import net.codestory.http.convert.TypeConvert;
import net.codestory.http.injection.GuiceAdapter;
import org.zapodot.jackson.java8.JavaOptionalModule;

public class WebApplication {
    private final WebServer webServer;

    public WebApplication(Module dependenciesModule) {
        configureObjectMapper();
        webServer = new WebServer(routes -> {
            routes.setIocAdapter(new GuiceAdapter(new WebApplicationModule(dependenciesModule)));
            routes.add(SimulationsResource.class);
            routes.add(SimulationResource.class);
        });
    }

    private void configureObjectMapper() {
        TypeConvert.overrideMapper(new ObjectMapper().registerModule(new JavaOptionalModule()));
    }

    public void start() {
        webServer.start();
    }

    public void startOnRandomPort() {
        webServer.startOnRandomPort();
    }

    public int port() {
        return webServer.port();
    }

    public void stop() {
        webServer.stop();
    }
}


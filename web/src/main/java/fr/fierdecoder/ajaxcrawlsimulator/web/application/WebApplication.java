package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.inject.Module;
import fr.fierdecoder.ajaxcrawlsimulator.web.resource.SimulationResource;
import fr.fierdecoder.ajaxcrawlsimulator.web.resource.SimulationsResource;
import net.codestory.http.WebServer;
import net.codestory.http.injection.GuiceAdapter;

public class WebApplication {
    private final WebServer webServer;

    public WebApplication(Module dependenciesModule) {
        webServer = new WebServer(routes -> {
            routes.setIocAdapter(new GuiceAdapter(new WebApplicationModule(dependenciesModule)));
            routes.add(SimulationsResource.class);
            routes.add(SimulationResource.class);
        });
    }

    public void start() {
        webServer.start();
    }
}


package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import fr.fierdecoder.ajaxcrawlsimulator.web.resource.SimulationResource;
import fr.fierdecoder.ajaxcrawlsimulator.web.resource.SimulationsResource;
import net.codestory.http.WebServer;
import net.codestory.http.injection.GuiceAdapter;

public class Application {
    public static void main(String[] args) {
        WebServer webServer = new WebServer(routes -> {
            routes.setIocAdapter(new GuiceAdapter(new ApplicationModule()));
            routes.add(SimulationsResource.class);
            routes.add(SimulationResource.class);
        });
        webServer.start();
    }
}

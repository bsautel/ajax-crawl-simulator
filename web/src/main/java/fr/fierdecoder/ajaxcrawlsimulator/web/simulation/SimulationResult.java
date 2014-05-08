package fr.fierdecoder.ajaxcrawlsimulator.web.simulation;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;

public class SimulationResult {
    private final Simulation simulation;
    private final WebPagesRegistry webPagesRegistry;

    public SimulationResult(Simulation simulation, WebPagesRegistry webPagesRegistry) {
        this.simulation = simulation;
        this.webPagesRegistry = webPagesRegistry;
    }

    public String getName() {
        return simulation.getName();
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public WebPagesRegistry getWebPagesRegistry() {
        return webPagesRegistry;
    }
}

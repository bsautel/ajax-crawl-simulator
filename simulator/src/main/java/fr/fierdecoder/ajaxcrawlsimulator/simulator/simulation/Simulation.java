package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;

public class Simulation {
    private final SimulationDescriptor simulationDescriptor;
    private final WebPagesRegistry webPagesRegistry;

    public Simulation(SimulationDescriptor simulationDescriptor, WebPagesRegistry webPagesRegistry) {
        this.simulationDescriptor = simulationDescriptor;
        this.webPagesRegistry = webPagesRegistry;
    }

    public String getName() {
        return simulationDescriptor.getName();
    }

    public SimulationDescriptor getDescriptor() {
        return simulationDescriptor;
    }

    public WebPagesRegistry getWebPagesRegistry() {
        return webPagesRegistry;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepository;

public class Simulation {
    private final SimulationDescriptor simulationDescriptor;
    private final WebPagesRepository webPagesRepository;

    public Simulation(SimulationDescriptor simulationDescriptor, WebPagesRepository webPagesRepository) {
        this.simulationDescriptor = simulationDescriptor;
        this.webPagesRepository = webPagesRepository;
    }

    public String getName() {
        return simulationDescriptor.getName();
    }

    public SimulationDescriptor getDescriptor() {
        return simulationDescriptor;
    }

    public WebPagesRepository getWebPagesRepository() {
        return webPagesRepository;
    }
}

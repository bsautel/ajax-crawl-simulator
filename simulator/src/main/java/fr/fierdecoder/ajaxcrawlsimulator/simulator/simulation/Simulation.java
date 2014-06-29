package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository.WebPagesRepository;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;

public class Simulation {
    private final SimulationDescriptor simulationDescriptor;
    private final WebPagesRepository webPagesRepository;
    private final CrawlState state;

    public Simulation(SimulationDescriptor simulationDescriptor, WebPagesRepository webPagesRepository, CrawlState state) {
        this.simulationDescriptor = simulationDescriptor;
        this.webPagesRepository = webPagesRepository;
        this.state = state;
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

    public CrawlState getState() {
        return state;
    }
}

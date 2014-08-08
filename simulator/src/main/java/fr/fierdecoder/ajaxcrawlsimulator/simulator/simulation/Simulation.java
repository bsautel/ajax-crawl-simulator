package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;

public class Simulation {
    private final SimulationDescriptor simulationDescriptor;
    private final CrawlState state;

    public Simulation(SimulationDescriptor simulationDescriptor, CrawlState state) {
        this.simulationDescriptor = simulationDescriptor;
        this.state = state;
    }

    public String getName() {
        return simulationDescriptor.getName();
    }

    public SimulationDescriptor getDescriptor() {
        return simulationDescriptor;
    }

    public CrawlState getState() {
        return state;
    }
}

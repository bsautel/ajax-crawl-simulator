package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import com.google.auto.value.AutoValue;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;

@AutoValue
public abstract class Simulation {
    public String getName() {
        return getDescriptor().getName();
    }

    public abstract SimulationDescriptor getDescriptor();

    public abstract CrawlState getState();

    public static Simulation create(SimulationDescriptor simulationDescriptor, CrawlState state) {
        return new AutoValue_Simulation(simulationDescriptor, state);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import com.google.auto.value.AutoValue;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepository;

@AutoValue
public abstract class Simulation {
    public String getName() {
        return getDescriptor().name();
    }

    public abstract SimulationDescriptor getDescriptor();

    public abstract WebPagesRepository getWebPagesRepository();

    public static Simulation create(SimulationDescriptor descriptor, WebPagesRepository repository) {
        return new AutoValue_Simulation(descriptor, repository);
    }
}

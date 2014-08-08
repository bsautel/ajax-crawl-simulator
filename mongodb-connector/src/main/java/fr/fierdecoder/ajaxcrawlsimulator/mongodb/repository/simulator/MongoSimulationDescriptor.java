package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;

public class MongoSimulationDescriptor {
    private final String name;
    private final String entryUrl;
    private final String urlPrefix;

    public MongoSimulationDescriptor(SimulationDescriptor descriptor) {
        this(descriptor.getName(), descriptor.getEntryUrl(), descriptor.getUrlPrefix());
    }

    @JsonCreator
    public MongoSimulationDescriptor(@JsonProperty("name") String name, @JsonProperty("entryUrl") String entryUrl,
                                     @JsonProperty("urlPrefix") String urlPrefix) {
        this.name = name;
        this.entryUrl = entryUrl;
        this.urlPrefix = urlPrefix;
    }

    public String getName() {
        return name;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public SimulationDescriptor toSimulationDescriptor() {
        return SimulationDescriptor.create(name, entryUrl, urlPrefix);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SimulationDescriptor {
    public abstract String name();

    public abstract String entryUrl();

    public abstract String urlPrefix();

    @JsonCreator
    public static SimulationDescriptor create(@JsonProperty("name") String name,
                                              @JsonProperty("entryUrl") String entryUrl,
                                              @JsonProperty("urlPrefix") String urlPrefix) {
        return new AutoValue_SimulationDescriptor(name, entryUrl, urlPrefix);
    }
}

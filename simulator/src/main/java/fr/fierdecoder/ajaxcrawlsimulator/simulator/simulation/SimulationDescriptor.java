package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import java.util.Objects;

public class SimulationDescriptor {
    private String name;
    private String entryUrl;
    private String urlPrefix;

    public SimulationDescriptor() {
    }

    public SimulationDescriptor(String name, String entryUrl, String urlPrefix) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimulationDescriptor that = (SimulationDescriptor) o;

        return Objects.equals(name, that.name)
                && Objects.equals(entryUrl, that.entryUrl)
                && Objects.equals(urlPrefix, that.urlPrefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, entryUrl, urlPrefix);
    }

    @Override
    public String toString() {
        return "SimulationDescriptor{" +
                "name='" + name + '\'' +
                ", entryUrl='" + entryUrl + '\'' +
                ", urlPrefix='" + urlPrefix + '\'' +
                '}';
    }
}

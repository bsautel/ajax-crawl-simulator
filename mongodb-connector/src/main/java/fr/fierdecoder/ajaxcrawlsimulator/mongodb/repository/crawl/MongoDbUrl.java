package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

public class MongoDbUrl {
    private String simulationName;
    private String url;

    public MongoDbUrl() {
    }

    public MongoDbUrl(String simulationName, String url) {
        this.simulationName = simulationName;
        this.url = url;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

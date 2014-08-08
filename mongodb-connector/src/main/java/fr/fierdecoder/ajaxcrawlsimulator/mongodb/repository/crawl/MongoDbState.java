package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

public class MongoDbState {
    private String simulationName;
    private boolean running;

    public MongoDbState() {
    }

    public MongoDbState(String simulationName, boolean running) {
        this.simulationName = simulationName;
        this.running = running;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

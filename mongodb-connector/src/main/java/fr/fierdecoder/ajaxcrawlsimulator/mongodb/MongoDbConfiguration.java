package fr.fierdecoder.ajaxcrawlsimulator.mongodb;

public class MongoDbConfiguration {
    private final String host;
    private final int port;
    private final String databaseName;

    public MongoDbConfiguration(String host, int port, String databaseName) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}

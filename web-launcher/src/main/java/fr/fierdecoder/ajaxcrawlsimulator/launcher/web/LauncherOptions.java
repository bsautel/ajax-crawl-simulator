package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.beust.jcommander.Parameter;

public class LauncherOptions {
    public static final int DEFAULT_HTTP_PORT = 8080;
    @Parameter(names = {"-p", "--httpPort"}, description = "HTTP Server Port")
    private int httpPort = DEFAULT_HTTP_PORT;
    public static final int DEFAULT_MONGO_DB_PORT = 27017;
    @Parameter(names = "--mongo-port", description = "Port of the MongoDB Database")
    private int mongoDbPort = DEFAULT_MONGO_DB_PORT;
    public static final String DEFAULT_MONGO_DB_HOST = "localhost";
    @Parameter(names = "--mongo-host", description = "Host of the MongoDB Database")
    private String mongoDbHost = DEFAULT_MONGO_DB_HOST;
    public static final String DEFAULT_MONGO_DB_DATABASE_NAME = "ajax-crawl-simulator";
    @Parameter(names = "--mongo-db-getName", description = "Name of the MongoDB Database")
    private String databaseName = DEFAULT_MONGO_DB_DATABASE_NAME;
    @Parameter(names = {"-h", "--help"}, description = "Display Help", help = true)
    private boolean help = false;
    @Parameter(names = "--mongo", description = "Use MongoDB as Storage Engine (default is in-memory without persistence)")
    private boolean mongoDb = false;

    public boolean isHelp() {
        return help;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public boolean isMongoDb() {
        return mongoDb;
    }

    public String getMongoDbHost() {
        return mongoDbHost;
    }

    public int getMongoDbPort() {
        return mongoDbPort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return "LauncherOptions{" +
                "help=" + help +
                ", httpPort=" + httpPort +
                ", mongoDb=" + mongoDb +
                ", mongoDbHost='" + mongoDbHost + '\'' +
                ", mongoDbPort=" + mongoDbPort +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.beust.jcommander.Parameter;

public class LauncherOptions {
    public static final int DEFAULT_HTTP_PORT = 8080;
    public static final int DEFAULT_MONGO_DB_PORT = 27017;
    public static final String DEFAULT_MONGO_DB_HOST = "localhost";
    public static final String DEFAULT_MONGO_DB_DATABASE_NAME = "ajax-crawl-simulator";

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help = false;

    @Parameter(names = {"-p", "--httpPort"}, description = "HTTP port (default is " + DEFAULT_HTTP_PORT + ")")
    private int httpPort = DEFAULT_HTTP_PORT;

    @Parameter(names = "--mongo", description = "Use MongoDB as storage engine (default is in-memory without persistence)")
    private boolean mongoDb = false;

    @Parameter(names = "--mongo-host", description = "Host of the MongoDB database (default is " + DEFAULT_MONGO_DB_HOST + ")")
    private String mongoDbHost = DEFAULT_MONGO_DB_HOST;

    @Parameter(names = "--mongo-port", description = "Port of the MongoDB database (default is " + DEFAULT_MONGO_DB_PORT + ")")
    private int mongoDbPort = DEFAULT_MONGO_DB_PORT;

    @Parameter(names = "--mongo-db-getName", description = "Name of the MongoDB database (default is " + DEFAULT_MONGO_DB_DATABASE_NAME + ")")
    private String databaseName = DEFAULT_MONGO_DB_DATABASE_NAME;

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

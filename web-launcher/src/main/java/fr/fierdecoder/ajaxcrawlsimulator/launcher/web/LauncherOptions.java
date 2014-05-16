package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.beust.jcommander.Parameter;

public class LauncherOptions {
    public static final int DEFAULT_PORT = 27017;
    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_DATABASE_NAME = "ajax-crawl-simulator";

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help;

    @Parameter(names = "--mongo", description = "Use MongoDB as storage engine (default is in-memory)")
    private boolean mongoDb = false;

    @Parameter(names = "--mongo-host", description = "Host of the MongoDB database (default is " + DEFAULT_HOST + ")")
    private String mongoDbHost = DEFAULT_HOST;

    @Parameter(names = "--mongo-port", description = "Port of the MongoDB database (default is " + DEFAULT_PORT + ")")
    private int mongoDbPort = DEFAULT_PORT;

    @Parameter(names = "--mongo-db-name", description = "Name of the MongoDB database (default is " + DEFAULT_DATABASE_NAME + ")")
    private String databaseName = DEFAULT_DATABASE_NAME;

    public boolean isHelp() {
        return help;
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
                "mongoDb=" + mongoDb +
                ", mongoDbHost='" + mongoDbHost + '\'' +
                ", mongoDbPort=" + mongoDbPort +
                ", databaseName='" + databaseName + '\'' +
                '}';
    }
}

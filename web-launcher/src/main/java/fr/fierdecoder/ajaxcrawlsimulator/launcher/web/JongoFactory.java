package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import org.jongo.Jongo;

import java.net.UnknownHostException;

public class JongoFactory {
    private final MongoDbConfiguration configuration;

    public JongoFactory(MongoDbConfiguration configuration) {
        this.configuration = configuration;
    }

    public Jongo createJongoConnection() {
        try {
            MongoClient mongoClient = new MongoClient(configuration.getHost(), configuration.getPort());
            DB database = mongoClient.getDB(configuration.getDatabaseName());
            return new Jongo(database);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

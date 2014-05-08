package fr.fierdecoder.ajaxcrawlsimulator.mongodb;

import com.google.inject.Inject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;

import java.net.UnknownHostException;

public class JongoConnectionFactory {
    private final MongoDbConfiguration configuration;

    @Inject
    public JongoConnectionFactory(MongoDbConfiguration configuration) {
        this.configuration = configuration;
    }

    public Jongo create() {
        try {
            MongoClient mongoClient = new MongoClient(configuration.getHost(), configuration.getPort());
            DB database = mongoClient.getDB(configuration.getDatabaseName());
            return new Jongo(database);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

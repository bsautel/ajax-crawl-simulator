package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import org.jongo.Jongo;

import java.net.UnknownHostException;

public class MongoDbWebPagesRegistryFactory implements WebPagesRegistryFactory {
    private final MongoDbConfiguration configuration;

    public MongoDbWebPagesRegistryFactory(MongoDbConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public WebPagesRegistry create(String name) {
        try {
            MongoClient mongoClient = new MongoClient(configuration.getHost(), configuration.getPort());
            DB database = mongoClient.getDB(configuration.getDatabaseName());
            Jongo jongo = new Jongo(database);
            return new MongoWebPagesRegistry(name, jongo);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

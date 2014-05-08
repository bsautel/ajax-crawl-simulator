package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import org.jongo.Jongo;

public class MongoDbWebPagesRegistryFactory implements WebPagesRegistryFactory {
    private final JongoConnectionFactory jongoConnectionFactory;

    @Inject
    public MongoDbWebPagesRegistryFactory(JongoConnectionFactory jongoConnectionFactory) {
        this.jongoConnectionFactory = jongoConnectionFactory;
    }

    @Override
    public WebPagesRegistry create(String name) {
        Jongo jongo = jongoConnectionFactory.create();
        return new MongoWebPagesRegistry(name, jongo);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import org.jongo.Jongo;

public class MongoDbCrawlStateFactory implements CrawlStateFactory {
    private final JongoConnectionFactory jongoConnectionFactory;

    @Inject
    public MongoDbCrawlStateFactory(JongoConnectionFactory jongoConnectionFactory) {
        this.jongoConnectionFactory = jongoConnectionFactory;
    }

    @Override
    public CrawlState create(String name) {
        Jongo jongo = jongoConnectionFactory.create();
        return new MongoDbCrawlState(name, jongo);
    }
}

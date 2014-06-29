package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepository;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import org.jongo.Jongo;

public class MongoDbWebPagesRepositoryFactory implements WebPagesRepositoryFactory {
    private final JongoConnectionFactory jongoConnectionFactory;

    @Inject
    public MongoDbWebPagesRepositoryFactory(JongoConnectionFactory jongoConnectionFactory) {
        this.jongoConnectionFactory = jongoConnectionFactory;
    }

    @Override
    public WebPagesRepository create(String name) {
        Jongo jongo = jongoConnectionFactory.create();
        return new MongoWebPagesRepository(name, jongo);
    }
}

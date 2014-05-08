package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MongoWebPagesRegistry implements WebPagesRegistry {
    public static final String ID_FILTER = "{'_id': '#'}";
    private final MongoWebPageConverter mongoWebPageConverter = new MongoWebPageConverter();
    private final MongoCollection collection;

    public MongoWebPagesRegistry(Jongo jongo) {
        collection = jongo.getCollection("webPages");
    }


    @Override
    public void register(WebPage page) {
        MongoWebPage mongoPage = mongoWebPageConverter.convertToMongo(page);
        collection.insert(mongoPage);
    }

    @Override
    public boolean containsUrl(String url) {
        long count = collection.count(ID_FILTER, url);
        return count != 0;
    }

    @Override
    public WebPage getByUrl(String url) {
        MongoWebPage mongoPage = collection.findOne(ID_FILTER, url).as(MongoWebPage.class);
        return mongoWebPageConverter.convertFromMongo(mongoPage);
    }

    @Override
    public long getPagesCount() {
        return collection.count();
    }

    @Override
    public Collection<WebPage> getWebPages() {
        Iterable<MongoWebPage> mongoWebPages = collection.find().as(MongoWebPage.class);
        Set<WebPage> result = new HashSet<>();
        mongoWebPages.forEach(mongoWebPage -> result.add(mongoWebPageConverter.convertFromMongo(mongoWebPage)));
        return result;
    }
}

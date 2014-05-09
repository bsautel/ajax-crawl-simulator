package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MongoWebPagesRegistry implements WebPagesRegistry {
    public static final String NAME_AND_URL_FILTER = "{'url': '#', 'simulationName': '#'}";
    public static final String SIMULATION_NAME_FILTER = "{'simulationName': '#'}";
    private final MongoWebPageConverter mongoWebPageConverter = new MongoWebPageConverter();
    private final MongoCollection collection;
    private final String simulationName;

    public MongoWebPagesRegistry(String simulationName, Jongo jongo) {
        this.simulationName = simulationName;
        collection = jongo.getCollection("webPages");
    }


    @Override
    public void register(WebPage page) {
        MongoWebPage mongoPage = mongoWebPageConverter.convertToMongo(page, simulationName);
        collection.insert(mongoPage);
    }

    @Override
    public boolean containsUrl(String url) {
        long count = collection.count(NAME_AND_URL_FILTER, url, simulationName);
        return count != 0;
    }

    @Override
    public WebPage getByUrl(String url) {
        MongoWebPage mongoPage = collection.findOne(NAME_AND_URL_FILTER, url, simulationName).as(MongoWebPage.class);
        return mongoWebPageConverter.convertFromMongo(mongoPage);
    }

    @Override
    public long getPagesCount() {
        return collection.count(SIMULATION_NAME_FILTER, simulationName);
    }

    @Override
    public Collection<WebPage> getWebPages() {
        Iterable<MongoWebPage> mongoWebPages = collection.
                find(SIMULATION_NAME_FILTER, simulationName)
                .as(MongoWebPage.class);
        Set<WebPage> result = new HashSet<>();
        mongoWebPages.forEach(mongoWebPage -> result.add(mongoWebPageConverter.convertFromMongo(mongoWebPage)));
        return result;
    }

    public void drop() {
        collection.remove(SIMULATION_NAME_FILTER, simulationName);
    }
}

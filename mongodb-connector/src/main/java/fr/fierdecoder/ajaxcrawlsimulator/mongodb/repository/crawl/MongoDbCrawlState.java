package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.WebPagePreviewConverter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toSet;

public class MongoDbCrawlState implements CrawlState {
    public static final String NAME_AND_URL_FILTER = "{'url': '#', 'simulationName': '#'}";
    public static final String SIMULATION_NAME_FILTER = "{'simulationName': '#'}";
    private final MongoWebPageConverter mongoWebPageConverter = new MongoWebPageConverter(new WebPageFactory());
    private final MongoCollection collection;
    private final String simulationName;

    public MongoDbCrawlState(String simulationName, Jongo jongo) {
        this.simulationName = simulationName;
        collection = jongo.getCollection("webPages");
        ensureIndexOnField("simulationName");
        ensureIndexOnField("url");
    }

    private void ensureIndexOnField(String field) {
        collection.ensureIndex("{" + field + ": 1}", "{unique: false}");
    }


    @Override
    public void addUrl(String url) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void addUrls(Collection<String> urls) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean hasUrlToCrawl() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getUrlToCrawl() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void maskAsFinished() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean isRunning() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void add(WebPage page) {
        MongoWebPage mongoPage = mongoWebPageConverter.convertToMongo(page, simulationName);
        collection.insert(mongoPage);
    }

    @Override
    public boolean containsUrl(String url) {
        long count = collection.count(NAME_AND_URL_FILTER, url, simulationName);
        return count != 0;
    }

    @Override
    public Optional<WebPage> getByUrl(String url) {
        MongoWebPage mongoPage = collection.findOne(NAME_AND_URL_FILTER, url, simulationName).as(MongoWebPage.class);
        if (mongoPage != null) {
            return Optional.of(mongoWebPageConverter.convertFromMongo(mongoPage));
        }
        return empty();
    }

    @Override
    public long getPagesCount() {
        return collection.count(SIMULATION_NAME_FILTER, simulationName);
    }

    @Override
    public Collection<WebPagePreview> getWebPagesPreviews() {
        Iterable<MongoWebPage> mongoWebPages = collection
                .find(SIMULATION_NAME_FILTER, simulationName)
                .projection("{body: 0, links: 0, targetUrl: 0}")
                .as(MongoWebPage.class);
        Set<WebPage> result = new HashSet<>();
        mongoWebPages.forEach(mongoWebPage -> result.add(mongoWebPageConverter.convertFromMongo(mongoWebPage)));
        return result.stream()
                .map(WebPagePreviewConverter::createWebPagePreview)
                .collect(toSet());
    }

    public void drop() {
        collection.remove(SIMULATION_NAME_FILTER, simulationName);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class MongoDbCrawlState implements CrawlState {
    public static final String ID_AND_NAME_FILTER = "{'_id': '#', 'simulationName': '#'}";
    public static final String URL_AND_NAME_FILTER = "{'url': '#', 'simulationName': '#'}";
    public static final String SIMULATION_NAME_FILTER = "{'simulationName': '#'}";
    public static final String SIMULATION_NAME = "simulationName";
    public static final String URL = "url";
    private final MongoDbWebPageConverter mongoWebPageConverter = new MongoDbWebPageConverter();
    private final MongoCollection pagesCollection;
    private final String simulationName;
    private final MongoCollection urlsCollection;
    private final MongoCollection stateCollection;

    public MongoDbCrawlState(String simulationName, Jongo jongo) {
        this.simulationName = simulationName;
        pagesCollection = jongo.getCollection("pages");
        urlsCollection = jongo.getCollection("urls");
        stateCollection = jongo.getCollection("states");
        ensureIndexes();
    }

    private void ensureIndexes() {
        ensurePageIndexOnField(pagesCollection, SIMULATION_NAME);
        ensurePageIndexOnField(pagesCollection, URL);
        ensurePageIndexOnField(urlsCollection, SIMULATION_NAME);
        ensurePageIndexOnField(urlsCollection, URL);
        ensurePageIndexOnField(stateCollection, SIMULATION_NAME);
    }

    private void ensurePageIndexOnField(MongoCollection collection, String field) {
        collection.ensureIndex("{" + field + ": 1}", "{unique: false}");
    }

    @Override
    public void addUrlToCrawl(String url) {
        urlsCollection.insert(new MongoDbUrl(simulationName, url));
    }

    @Override
    public void addUrlsToCrawl(Collection<String> urls) {
        urls.forEach(this::addUrlToCrawl);
    }

    @Override
    public boolean hasUrlToCrawl() {
        return urlsCollection.count(SIMULATION_NAME_FILTER, simulationName) > 0;
    }

    @Override
    public String getUrlToCrawl() {
        MongoDbUrl url = urlsCollection
                .findOne(SIMULATION_NAME_FILTER, simulationName)
                .as(MongoDbUrl.class);
        urlsCollection.remove(URL_AND_NAME_FILTER, url.getUrl(), simulationName);
        return url.getUrl();
    }

    @Override
    public void maskAsFinished() {
        Optional<MongoDbState> optionalState = findState();
        if (optionalState.isPresent()) {
            MongoDbState state = optionalState.get();
            state.setRunning(false);
            stateCollection.update(SIMULATION_NAME_FILTER, simulationName).with(state);
        } else {
            MongoDbState state = new MongoDbState(simulationName, false);
            stateCollection.insert(state);
        }
    }

    @Override
    public boolean isRunning() {
        Optional<MongoDbState> optionalState = findState();
        return optionalState.map(MongoDbState::isRunning).orElse(true);
    }

    private Optional<MongoDbState> findState() {
        MongoDbState state = stateCollection
                .findOne(SIMULATION_NAME_FILTER, simulationName)
                .as(MongoDbState.class);
        return ofNullable(state);
    }

    @Override
    public void addPage(WebPage page) {
        MongoDbWebPage mongoPage = mongoWebPageConverter.convertToMongo(page, simulationName);
        pagesCollection.insert(mongoPage);
    }

    @Override
    public boolean containsUrl(String url) {
        long count = pagesCollection.count(URL_AND_NAME_FILTER, url, simulationName);
        return count != 0;
    }

    @Override
    public Optional<WebPage> getPageByUrl(String url) {
        MongoDbWebPage mongoPage = pagesCollection
                .findOne(URL_AND_NAME_FILTER, url, simulationName)
                .as(MongoDbWebPage.class);
        return ofNullable(mongoPage).map(mongoWebPageConverter::convertFromMongo);
    }

    @Override
    public boolean containsPage(String id) {
        long count = pagesCollection.count(ID_AND_NAME_FILTER, id, simulationName);
        return count != 0;
    }

    @Override
    public Optional<WebPage> getPageById(String id) {
        MongoDbWebPage mongoPage = pagesCollection
                .findOne(ID_AND_NAME_FILTER, id, simulationName)
                .as(MongoDbWebPage.class);
        return ofNullable(mongoPage).map(mongoWebPageConverter::convertFromMongo);
    }

    @Override
    public long getPagesCount() {
        return pagesCollection.count(SIMULATION_NAME_FILTER, simulationName);
    }

    @Override
    public Collection<WebPagePreview> getWebPagesPreviews() {
        Iterable<MongoDbWebPage> mongoWebPages = pagesCollection
                .find(SIMULATION_NAME_FILTER, simulationName)
                .projection("{body: 0, links: 0, targetUrl: 0}")
                .as(MongoDbWebPage.class);
        Set<WebPagePreview> result = new HashSet<>();
        mongoWebPages.forEach(mongoWebPage -> result.add(mongoWebPageConverter.convertToPreview(mongoWebPage)));
        return result;
    }

    @Override
    public void drop() {
        pagesCollection.remove(SIMULATION_NAME_FILTER, simulationName);
    }
}

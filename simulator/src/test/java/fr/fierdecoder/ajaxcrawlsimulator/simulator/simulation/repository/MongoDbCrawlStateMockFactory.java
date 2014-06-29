package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class MongoDbCrawlStateMockFactory implements CrawlStateFactory {
    private final Map<String, CrawlState> crawlStateMap = new HashMap<>();

    @Override
    public CrawlState create(String name) {
        return crawlStateMap.computeIfAbsent(name, n -> mock(CrawlState.class));
    }
}

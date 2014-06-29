package fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;

public interface Crawler {
    void crawl(CrawlPerimeter crawlPerimeter, CrawlState state);
}

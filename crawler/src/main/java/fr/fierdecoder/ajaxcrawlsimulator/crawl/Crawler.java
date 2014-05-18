package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;

/**
 * Created by benoit on 18/05/14.
 */
public interface Crawler {
    void crawl(CrawlPerimeter crawlPerimeter, WebPagesRegistry registry);
}

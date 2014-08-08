package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

public interface CrawlStateFactory {
    CrawlState create(String name);
}

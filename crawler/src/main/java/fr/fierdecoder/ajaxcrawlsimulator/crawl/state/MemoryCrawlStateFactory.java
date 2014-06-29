package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

public class MemoryCrawlStateFactory implements CrawlStateFactory {
    @Override
    public CrawlState create(String name) {
        return new MemoryCrawlState();
    }
}

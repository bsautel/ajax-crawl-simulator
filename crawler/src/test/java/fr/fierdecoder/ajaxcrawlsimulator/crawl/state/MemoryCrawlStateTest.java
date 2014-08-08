package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

public class MemoryCrawlStateTest extends AbstractCrawlStateTest {
    @Override
    protected CrawlState createCrawlState(String aDefault) {
        return new MemoryCrawlState();
    }
}
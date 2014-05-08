package fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter;

public class SimpleCrawlPerimeter implements CrawlPerimeter {
    private final String entryUrl;

    public SimpleCrawlPerimeter(String entryUrl) {
        this.entryUrl = entryUrl;
    }

    @Override
    public String getEntryUrl() {
        return entryUrl;
    }

    @Override
    public boolean contains(String url) {
        return true;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter;

public class SimpleCrawlPerimeter implements CrawlPerimeter {
    private final String entryUrl;
    private final String prefix;

    public SimpleCrawlPerimeter(String entryUrl, String prefix) {
        this.entryUrl = entryUrl;
        this.prefix = prefix;
    }

    @Override
    public String getEntryUrl() {
        return entryUrl;
    }

    @Override
    public boolean contains(String url) {
        return url.startsWith(prefix);
    }
}

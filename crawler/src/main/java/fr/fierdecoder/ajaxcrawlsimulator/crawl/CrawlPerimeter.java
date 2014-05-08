package fr.fierdecoder.ajaxcrawlsimulator.crawl;

public class CrawlPerimeter {
    private final String entryUrl;

    public CrawlPerimeter(String entryUrl) {
        this.entryUrl = entryUrl;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public boolean contains(String url) {
        return true;
    }
}

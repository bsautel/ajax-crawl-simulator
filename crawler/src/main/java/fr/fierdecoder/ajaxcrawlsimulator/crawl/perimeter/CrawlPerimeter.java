package fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter;

public interface CrawlPerimeter {
    String getEntryUrl();

    boolean contains(String url);
}

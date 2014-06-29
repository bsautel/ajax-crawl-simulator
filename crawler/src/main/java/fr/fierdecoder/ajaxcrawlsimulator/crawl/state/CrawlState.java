package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import java.util.Collection;

public interface CrawlState {
    void addUrl(String url);

    void addUrls(Collection<String> urls);

    boolean hasUrlToCrawl();

    String getUrlToCrawl();

    void maskAsFinished();

    boolean isRunning();
}

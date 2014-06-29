package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

import java.util.Collection;
import java.util.Optional;

public interface CrawlState {
    void addUrl(String url);

    void addUrls(Collection<String> urls);

    boolean hasUrlToCrawl();

    String getUrlToCrawl();

    void maskAsFinished();

    boolean isRunning();

    void add(WebPage page);

    boolean containsUrl(String url);

    Optional<WebPage> getByUrl(String url);

    long getPagesCount();

    Collection<WebPagePreview> getWebPagesPreviews();
}

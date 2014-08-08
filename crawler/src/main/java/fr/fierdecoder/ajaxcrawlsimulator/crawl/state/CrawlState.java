package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

import java.util.Collection;
import java.util.Optional;

public interface CrawlState {
    void addUrlToCrawl(String url);

    void addUrlsToCrawl(Collection<String> urls);

    boolean hasUrlToCrawl();

    String getUrlToCrawl();

    void maskAsFinished();

    boolean isRunning();

    void addPage(WebPage page);

    boolean containsPage(String url);

    Optional<WebPage> getPageByUrl(String url);

    long getPagesCount();

    Collection<WebPagePreview> getWebPagesPreviews();

    void drop();
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.repository;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPagePreview;

import java.util.Collection;
import java.util.Optional;

public interface WebPagesRepository {
    void add(WebPage page);

    boolean containsUrl(String url);

    Optional<WebPage> getByUrl(String url);

    long getPagesCount();

    Collection<WebPagePreview> getWebPagesPreviews();
}

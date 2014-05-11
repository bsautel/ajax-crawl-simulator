package fr.fierdecoder.ajaxcrawlsimulator.crawl.registry;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import java.util.Collection;
import java.util.Optional;

public interface WebPagesRegistry {
    void register(WebPage page);

    boolean containsUrl(String url);

    Optional<WebPage> getByUrl(String url);

    long getPagesCount();

    Collection<WebPage> getWebPages();
}

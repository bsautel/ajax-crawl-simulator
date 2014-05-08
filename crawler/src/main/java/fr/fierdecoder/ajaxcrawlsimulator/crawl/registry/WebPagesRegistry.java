package fr.fierdecoder.ajaxcrawlsimulator.crawl.registry;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import java.util.Collection;

public interface WebPagesRegistry {
    void register(WebPage page);

    boolean containsUrl(String url);

    WebPage getByUrl(String url);

    int getPagesCount();

    Collection<WebPage> getWebPages();
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class PagesRegistry {
    private final Map<String, WebPage> pagesByUrl;

    private PagesRegistry(PagesRegistry other) {
        this.pagesByUrl = newHashMap(other.pagesByUrl);
    }

    public PagesRegistry() {
        pagesByUrl = newHashMap();
    }

    public PagesRegistry register(WebPage page) {
        PagesRegistry copy = new PagesRegistry(this);
        copy.pagesByUrl.put(page.getUrl(), page);
        return copy;
    }

    public boolean containsUrl(String url) {
        return pagesByUrl.containsKey(url);
    }

    public WebPage getByUrl(String url) {
        return pagesByUrl.get(url);
    }
}

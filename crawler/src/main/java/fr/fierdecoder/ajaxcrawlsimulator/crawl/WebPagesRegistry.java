package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class WebPagesRegistry {
    private final Map<String, WebPage> pagesByUrl;

    private WebPagesRegistry(WebPagesRegistry other) {
        this.pagesByUrl = newHashMap(other.pagesByUrl);
    }

    public WebPagesRegistry() {
        pagesByUrl = newHashMap();
    }

    public WebPagesRegistry register(WebPage page) {
        WebPagesRegistry copy = new WebPagesRegistry(this);
        copy.pagesByUrl.put(page.getUrl(), page);
        return copy;
    }

    public boolean containsUrl(String url) {
        return pagesByUrl.containsKey(url);
    }

    public WebPage getByUrl(String url) {
        return pagesByUrl.get(url);
    }

    public Object getPagesCount() {
        return pagesByUrl.size();
    }
}

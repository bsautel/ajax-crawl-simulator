package fr.fierdecoder.ajaxcrawlsimulator.crawl.registry;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryWebPagesRegistry implements WebPagesRegistry {
    private final Map<String, WebPage> pagesByUrl= new ConcurrentHashMap<>();

    @Override
    public void register(WebPage page) {
        pagesByUrl.put(page.getUrl(), page);
    }

    @Override
    public boolean containsUrl(String url) {
        return pagesByUrl.containsKey(url);
    }

    @Override
    public Optional<WebPage> getByUrl(String url) {
        return Optional.of(pagesByUrl.get(url));
    }

    @Override
    public long getPagesCount() {
        return pagesByUrl.size();
    }

    @Override
    public Collection<WebPage> getWebPages() {
        return pagesByUrl.values();
    }
}

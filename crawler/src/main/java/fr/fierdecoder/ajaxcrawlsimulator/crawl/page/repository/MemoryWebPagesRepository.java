package fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPagePreview;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

public class MemoryWebPagesRepository implements WebPagesRepository {
    private final Map<String, WebPage> pagesByUrl = new ConcurrentHashMap<>();

    @Override
    public void add(WebPage page) {
        pagesByUrl.put(page.getUrl(), page);
    }

    @Override
    public boolean containsUrl(String url) {
        return pagesByUrl.containsKey(url);
    }

    @Override
    public Optional<WebPage> getByUrl(String url) {
        return of(pagesByUrl.get(url));
    }

    @Override
    public long getPagesCount() {
        return pagesByUrl.size();
    }

    @Override
    public Collection<WebPagePreview> getWebPagesPreviews() {
        return pagesByUrl.values().stream()
                .map(WebPagePreviewConverter::createWebPagePreview)
                .collect(toSet());
    }
}

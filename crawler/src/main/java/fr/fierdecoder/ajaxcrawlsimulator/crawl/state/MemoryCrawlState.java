package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

public class MemoryCrawlState implements CrawlState {
    private final Map<String, WebPage> pagesById = new ConcurrentHashMap<>();
    private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void addUrlToCrawl(String url) {
        urlQueue.add(url);
    }

    @Override
    public void addUrlsToCrawl(Collection<String> urls) {
        urlQueue.addAll(urls);
    }

    @Override
    public boolean hasUrlToCrawl() {
        return !urlQueue.isEmpty();
    }

    @Override
    public String getUrlToCrawl() {
        return urlQueue.poll();
    }

    @Override
    public void maskAsFinished() {
        running.set(false);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void addPage(WebPage page) {
        pagesById.put(page.getId(), page);
    }

    @Override
    public boolean containsUrl(String url) {
        return pagesById.values().stream()
                .anyMatch(page -> page.getUrl().equals(url));
    }

    @Override
    public Optional<WebPage> getPageByUrl(String url) {
        return pagesById.values().stream()
                .filter(page -> page.getUrl().equals(url))
                .findFirst();
    }

    @Override
    public boolean containsPage(String id) {
        return pagesById.containsKey(id);
    }

    @Override
    public Optional<WebPage> getPageById(String id) {
        return of(pagesById.get(id));
    }

    @Override
    public long getPagesCount() {
        return pagesById.size();
    }

    @Override
    public Collection<WebPagePreview> getWebPagesPreviews() {
        return pagesById.values().stream()
                .map(WebPagePreviewConverter::createWebPagePreview)
                .collect(toSet());
    }

    @Override
    public void drop() {
        pagesById.clear();
        urlQueue.clear();
    }
}

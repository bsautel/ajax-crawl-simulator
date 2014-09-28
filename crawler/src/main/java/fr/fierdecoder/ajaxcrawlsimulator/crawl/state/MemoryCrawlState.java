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

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

public class MemoryCrawlState implements CrawlState {
    private final Map<String, WebPage> pagesByUrl = new ConcurrentHashMap<>();
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
        pagesByUrl.put(page.getUrl(), page);
    }

    @Override
    public boolean containsPage(String url) {
        return pagesByUrl.containsKey(url);
    }

    @Override
    public Optional<WebPage> getPageByUrl(String url) {
        return ofNullable(pagesByUrl.get(url));
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

    @Override
    public void drop() {
        pagesByUrl.clear();
        urlQueue.clear();
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class MemoryCrawlState implements CrawlState {
    private final Queue<String> urlQueue;
    private boolean running = true;

    public MemoryCrawlState() {
        this.urlQueue = new LinkedList<>();
    }

    @Override
    public void addUrl(String url) {
        urlQueue.add(url);
    }

    @Override
    public void addUrls(Collection<String> urls) {
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
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.repository;

public interface WebPagesRepositoryFactory {
    WebPagesRepository create(String name);
}

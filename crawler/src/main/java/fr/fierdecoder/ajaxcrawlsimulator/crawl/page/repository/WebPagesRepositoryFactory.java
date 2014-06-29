package fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository;

public interface WebPagesRepositoryFactory {
    WebPagesRepository create(String name);
}

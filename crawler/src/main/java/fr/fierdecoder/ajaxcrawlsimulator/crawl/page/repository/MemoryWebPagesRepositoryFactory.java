package fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository;

public class MemoryWebPagesRepositoryFactory implements WebPagesRepositoryFactory {
    @Override
    public WebPagesRepository create(String name) {
        return new MemoryWebPagesRepository();
    }
}

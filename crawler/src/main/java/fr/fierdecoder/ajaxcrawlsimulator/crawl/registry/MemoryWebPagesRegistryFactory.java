package fr.fierdecoder.ajaxcrawlsimulator.crawl.registry;

public class MemoryWebPagesRegistryFactory implements WebPagesRegistryFactory {
    @Override
    public WebPagesRegistry create(String name) {
        return new MemoryWebPagesRegistry();
    }
}

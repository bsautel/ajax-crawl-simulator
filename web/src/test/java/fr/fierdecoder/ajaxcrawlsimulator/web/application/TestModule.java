package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.MemoryWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.MemorySimulationsRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.SimulationRegistry;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SimulationRegistry.class).to(MemorySimulationsRegistry.class).in(Singleton.class);
        bind(WebPagesRegistryFactory.class).to(MemoryWebPagesRegistryFactory.class).in(Singleton.class);
        bind(Crawler.class).to(CrawlerStub.class).in(Singleton.class);
    }
}

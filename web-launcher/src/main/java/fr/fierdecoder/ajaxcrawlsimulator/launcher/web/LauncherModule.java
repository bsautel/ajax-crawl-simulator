package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.guice.CrawlModule;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.MemoryWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoDbWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.simulator.MongoSimulationRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.MemorySimulationsRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.SimulationRegistry;

public class LauncherModule extends AbstractModule {
    private final LauncherOptions options;

    public LauncherModule(LauncherOptions options) {
        this.options = options;
    }

    @Override
    protected void configure() {
        if (options.isMongoDb()) {
            bindJongoFactory();
            bind(SimulationRegistry.class).to(MongoSimulationRegistry.class).in(Singleton.class);
            bind(WebPagesRegistryFactory.class).to(MongoDbWebPagesRegistryFactory.class).in(Singleton.class);
        } else {
            bind(SimulationRegistry.class).to(MemorySimulationsRegistry.class).in(Singleton.class);
            bind(WebPagesRegistryFactory.class).to(MemoryWebPagesRegistryFactory.class).in(Singleton.class);
        }
        install(new CrawlModule());
    }

    private void bindJongoFactory() {
        MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration(options.getMongoDbHost(),
                options.getMongoDbPort(), options.getDatabaseName());
        bind(MongoDbConfiguration.class).toInstance(mongoDbConfiguration);
    }
}

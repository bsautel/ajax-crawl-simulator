package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.guice.CrawlModule;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.MemoryWebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbWebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator.MongoSimulationRepository;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.MemorySimulationsRepository;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;

public class LauncherModule extends AbstractModule {
    private final LauncherOptions options;

    public LauncherModule(LauncherOptions options) {
        this.options = options;
    }

    @Override
    protected void configure() {
        if (options.isMongoDb()) {
            bindJongoFactory();
            bind(SimulationRepository.class).to(MongoSimulationRepository.class).in(Singleton.class);
            bind(WebPagesRepositoryFactory.class).to(MongoDbWebPagesRepositoryFactory.class).in(Singleton.class);
        } else {
            bind(SimulationRepository.class).to(MemorySimulationsRepository.class).in(Singleton.class);
            bind(WebPagesRepositoryFactory.class).to(MemoryWebPagesRepositoryFactory.class).in(Singleton.class);
        }
        install(new CrawlModule());
    }

    private void bindJongoFactory() {
        MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration(options.getMongoDbHost(),
                options.getMongoDbPort(), options.getDatabaseName());
        bind(MongoDbConfiguration.class).toInstance(mongoDbConfiguration);
    }
}

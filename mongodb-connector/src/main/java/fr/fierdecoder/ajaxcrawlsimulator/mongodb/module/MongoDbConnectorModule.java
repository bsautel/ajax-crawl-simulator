package fr.fierdecoder.ajaxcrawlsimulator.mongodb.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbCrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator.MongoDbSimulationRepository;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;

public class MongoDbConnectorModule extends AbstractModule {
    private final MongoDbConfiguration configuration;

    public MongoDbConnectorModule(MongoDbConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bindJongoFactory();
        bind(SimulationRepository.class).to(MongoDbSimulationRepository.class).in(Singleton.class);
        bind(CrawlStateFactory.class).to(MongoDbCrawlStateFactory.class).in(Singleton.class);
    }

    private void bindJongoFactory() {
        bind(MongoDbConfiguration.class).toInstance(configuration);
    }
}

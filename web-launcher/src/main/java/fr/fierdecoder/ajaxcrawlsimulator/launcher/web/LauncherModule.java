package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.google.inject.AbstractModule;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.guice.CrawlModule;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.module.MongoDbConnectorModule;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.module.MemorySimulatorModule;

public class LauncherModule extends AbstractModule {
    private final LauncherOptions options;

    public LauncherModule(LauncherOptions options) {
        this.options = options;
    }

    @Override
    protected void configure() {
        if (options.isMongoDb()) {
            MongoDbConfiguration configuration = new MongoDbConfiguration(options.getMongoDbHost(),
                    options.getMongoDbPort(), options.getDatabaseName());
            install(new MongoDbConnectorModule(configuration));
        } else {
            install(new MemorySimulatorModule());
        }
        install(new CrawlModule());
    }
}

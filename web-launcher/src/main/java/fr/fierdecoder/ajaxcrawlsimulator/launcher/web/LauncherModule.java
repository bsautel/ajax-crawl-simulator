package fr.fierdecoder.ajaxcrawlsimulator.launcher.web;

import com.google.inject.AbstractModule;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.guice.CrawlModule;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.MemoryWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.MongoDbConfiguration;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.MongoDbWebPagesRegistryFactory;

public class LauncherModule extends AbstractModule {
    private final LauncherOptions options;

    public LauncherModule(LauncherOptions options) {
        this.options = options;
    }

    @Override
    protected void configure() {
        bind(WebPagesRegistryFactory.class).toInstance(createWebPagesRegistryFactory());
        install(new CrawlModule());
    }

    private WebPagesRegistryFactory createWebPagesRegistryFactory() {
        if(options.isMongoDb()) {
            MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration(options.getMongoDbHost(),
                    options.getMongoDbPort(), options.getDatabaseName());
            return new MongoDbWebPagesRegistryFactory(mongoDbConfiguration);
        }
        return new MemoryWebPagesRegistryFactory();
    }
}

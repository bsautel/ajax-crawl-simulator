package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class WebApplicationModule extends AbstractModule {
    private final Module crawlModule;

    public WebApplicationModule(Module crawlModule) {
        this.crawlModule = crawlModule;
    }

    @Override
    protected void configure() {
        install(crawlModule);
    }
}

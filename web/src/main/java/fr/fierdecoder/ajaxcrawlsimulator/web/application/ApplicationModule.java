package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.guice.CrawlModule;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new CrawlModule());
    }
}

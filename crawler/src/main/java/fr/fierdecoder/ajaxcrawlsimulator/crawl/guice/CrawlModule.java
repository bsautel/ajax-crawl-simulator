package fr.fierdecoder.ajaxcrawlsimulator.crawl.guice;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler.NetworkCrawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.NetworkPageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;

public class CrawlModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PageReader.class).to(NetworkPageReader.class).in(Singleton.class);
        bind(Crawler.class).to(NetworkCrawler.class).in(Singleton.class);
    }
}

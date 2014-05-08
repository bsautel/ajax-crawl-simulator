package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;


public interface PageReader {
    WebPage readPage(String url);
}

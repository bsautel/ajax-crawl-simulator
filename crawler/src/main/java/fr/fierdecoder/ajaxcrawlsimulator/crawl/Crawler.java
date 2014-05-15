package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

import static org.slf4j.LoggerFactory.getLogger;

public class Crawler {
    private final PageReader pageReader;
    private static final Logger LOGGER = getLogger(Crawler.class);

    @Inject
    public Crawler(PageReader pageReader) {
        this.pageReader = pageReader;
    }

    public void crawl(CrawlPerimeter crawlPerimeter, WebPagesRegistry registry) {
        Queue<String> urlQueue = new LinkedList<>();
        urlQueue.add(crawlPerimeter.getEntryUrl());
        while (!urlQueue.isEmpty()) {
            crawlNextUrl(urlQueue, registry, crawlPerimeter);
        }
    }

    private void crawlNextUrl(Queue<String> urlQueue, WebPagesRegistry registry, CrawlPerimeter crawlPerimeter) {
        String url = urlQueue.poll();
        LOGGER.info("Crawling {}", url);
        if (mustBeCrawled(url, registry, crawlPerimeter)) {
            WebPage page = pageReader.readPage(url);
            if (page.isHtml()) {
                HtmlWebPage htmlPage = page.asHtml();
                urlQueue.addAll(htmlPage.getLinks());
                LOGGER.info("Url {} returned a HTML page with title {}", url, htmlPage.getTitle());
            } else if (page.isRedirection()) {
                RedirectionWebPage redirection = page.asRedirection();
                urlQueue.add(redirection.getTargetUrl());
                LOGGER.info("Url {} returned a redirection to {}", url, redirection.getTargetUrl());
            } else if (page.isUnreachable()) {
                LOGGER.info("Url {} is unreachable", url);
            }
            registry.register(page);
        } else {
            LOGGER.info("Url {} ignored since it is not in the crawl perimeter", url);
        }
    }

    private boolean mustBeCrawled(String url, WebPagesRegistry registry, CrawlPerimeter crawlPerimeter) {
        return !registry.containsUrl(url) && crawlPerimeter.contains(url);
    }
}

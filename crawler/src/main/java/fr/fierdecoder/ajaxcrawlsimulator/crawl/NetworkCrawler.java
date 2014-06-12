package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.slf4j.LoggerFactory.getLogger;

public class NetworkCrawler implements Crawler {
    private final PageReader pageReader;
    private static final Logger LOGGER = getLogger(NetworkCrawler.class);

    @Inject
    public NetworkCrawler(PageReader pageReader) {
        this.pageReader = pageReader;
    }

    @Override
    public void crawl(CrawlPerimeter crawlPerimeter, WebPagesRegistry registry) {
        Queue<String> urlQueue = new LinkedList<>();
        urlQueue.add(crawlPerimeter.getEntryUrl());
        while (!urlQueue.isEmpty()) {
            String url = urlQueue.poll();
            Collection<String> newUrls = crawlUrlIfNeeded(url, registry, crawlPerimeter);
            urlQueue.addAll(newUrls);
        }
    }

    private Collection<String> crawlUrlIfNeeded(String url, WebPagesRegistry registry, CrawlPerimeter crawlPerimeter) {
        LOGGER.info("Crawling {}", url);
        if (mustBeCrawled(url, registry, crawlPerimeter)) {
            return crawlUrl(url, registry);
        }
        LOGGER.info("Url {} ignored since it is not in the crawl perimeter", url);
        return emptyList();
    }

    private Collection<String> crawlUrl(String url, WebPagesRegistry registry) {
        WebPage page = pageReader.readPage(url);
        registry.register(page);
        if (page.isHtml()) {
            HtmlWebPage htmlPage = page.asHtml();
            LOGGER.info("Url {} returned a HTML page with title {}", url, htmlPage.getTitle());
            return htmlPage.getLinks();
        } else if (page.isRedirection()) {
            RedirectionWebPage redirection = page.asRedirection();
            LOGGER.info("Url {} returned a redirection to {}", url, redirection.getTargetUrl());
            return asList(redirection.getTargetUrl());
        } else if (page.isUnreachable()) {
            LOGGER.info("Url {} is unreachable", url);
            return emptyList();
        }
        throw new IllegalStateException("Unknown page type");
    }

    private boolean mustBeCrawled(String url, WebPagesRegistry registry, CrawlPerimeter crawlPerimeter) {
        return !registry.containsUrl(url) && crawlPerimeter.contains(url);
    }
}

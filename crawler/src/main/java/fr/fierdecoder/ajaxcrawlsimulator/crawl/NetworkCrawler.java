package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepository;
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
    public void crawl(CrawlPerimeter crawlPerimeter, WebPagesRepository repository) {
        Queue<String> urlQueue = new LinkedList<>();
        urlQueue.add(crawlPerimeter.getEntryUrl());
        while (!urlQueue.isEmpty()) {
            String url = urlQueue.poll();
            Collection<String> newUrls = crawlUrlIfNeeded(url, repository, crawlPerimeter);
            urlQueue.addAll(newUrls);
        }
    }

    private Collection<String> crawlUrlIfNeeded(String url, WebPagesRepository repository, CrawlPerimeter crawlPerimeter) {
        LOGGER.info("Crawling {}", url);
        if (mustBeCrawled(url, repository, crawlPerimeter)) {
            return crawlUrl(url, repository);
        }
        LOGGER.info("Url {} ignored since it is not in the crawl perimeter", url);
        return emptyList();
    }

    private Collection<String> crawlUrl(String url, WebPagesRepository repository) {
        WebPage page = pageReader.readPage(url);
        repository.add(page);
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

    private boolean mustBeCrawled(String url, WebPagesRepository repository, CrawlPerimeter crawlPerimeter) {
        return !repository.containsUrl(url) && crawlPerimeter.contains(url);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;

import java.util.LinkedList;
import java.util.Queue;

public class Crawler {
    private final PageReader pageReader;

    @Inject
    public Crawler(PageReader pageReader) {
        this.pageReader = pageReader;
    }

    public WebPagesRegistry crawl(CrawlPerimeter crawlPerimeter) {
        Queue<String> urlQueue = new LinkedList<>();
        WebPagesRegistry registry = new WebPagesRegistry();
        urlQueue.add(crawlPerimeter.getEntryUrl());
        while (!urlQueue.isEmpty()) {
            registry = crawlNextUrl(urlQueue, registry, crawlPerimeter);
        }
        return registry;
    }

    private WebPagesRegistry crawlNextUrl(Queue<String> urlQueue, WebPagesRegistry registry, CrawlPerimeter crawlPerimeter) {
        String url = urlQueue.poll();
        if (mustBeCrawled(url, registry, crawlPerimeter)) {
            WebPage page = pageReader.readPage(url);
            if (page.isHtmlWebPage()) {
                HtmlWebPage htmlPage = (HtmlWebPage) page;
                urlQueue.addAll(htmlPage.getLinks());
            } else if (page.isRedirection()) {
                RedirectionWebPage redirection = (RedirectionWebPage) page;
                urlQueue.add(redirection.getTargetUrl());
            }
            return registry.register(page);
        }
        return registry;
    }

    private boolean mustBeCrawled(String url, WebPagesRegistry registry, CrawlPerimeter crawlPerimeter) {
        return !registry.containsUrl(url) && crawlPerimeter.contains(url);
    }
}

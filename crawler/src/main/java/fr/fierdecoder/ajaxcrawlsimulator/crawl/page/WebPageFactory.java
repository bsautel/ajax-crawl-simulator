package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import com.google.inject.Singleton;

import java.util.Set;

import static fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageType.UNREACHABLE;

@Singleton
public class WebPageFactory {
    public HtmlWebPage buildHtmlWebPage(String url, int httpStatus, String title, String body, Set<String> links) {
        return new HtmlWebPage(url, httpStatus, title, body, links);
    }

    public RedirectionWebPage buildRedirectionWebPage(String url, int httpStatus, String body, String targetUrl) {
        return new RedirectionWebPage(url, httpStatus, body, targetUrl);
    }

    public WebPage buildUnreachableWebPage(String url, int httpStatus, String body) {
        return new WebPage(UNREACHABLE, url, httpStatus, body);
    }
}

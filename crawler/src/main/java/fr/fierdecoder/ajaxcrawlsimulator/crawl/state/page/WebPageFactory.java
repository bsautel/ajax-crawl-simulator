package fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page;

import com.google.inject.Singleton;

import java.util.Set;

@Singleton
public class WebPageFactory {
    public HtmlWebPage buildHtmlWebPage(String url, int httpStatus, String title, String body, Set<String> links) {
        return new HtmlWebPage(url, httpStatus, title, body, links);
    }

    public RedirectionWebPage buildRedirectionWebPage(String url, int httpStatus, String body, String targetUrl) {
        return new RedirectionWebPage(url, httpStatus, body, targetUrl);
    }

    public WebPage buildUnreachableWebPage(String url, int httpStatus, String body) {
        return new WebPage(WebPageType.UNREACHABLE, url, httpStatus, body);
    }

    public WebPage buildTextWebPage(String url, int httpStatus, String body) {
        return new WebPage(WebPageType.TEXT, url, httpStatus, body);
    }

    public WebPage buildBinaryWebPage(String url, int httpStatus) {
        return new WebPage(WebPageType.BINARY, url, httpStatus, "");
    }
}

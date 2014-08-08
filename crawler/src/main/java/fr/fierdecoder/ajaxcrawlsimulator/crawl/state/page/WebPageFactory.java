package fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page;

import com.google.inject.Singleton;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageType.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Singleton
public class WebPageFactory {
    public WebPage buildHtmlWebPage(String url, int httpStatus, String title, String body, Set<String> links) {
        return WebPage.create(url, httpStatus, body, HTML, of(title), links, empty());
    }

    public WebPage buildRedirectionWebPage(String url, int httpStatus, String body, String targetUrl) {
        return WebPage.create(url, httpStatus, body, REDIRECTION, empty(), newHashSet(), of(targetUrl));
    }

    public WebPage buildUnreachableWebPage(String url, int httpStatus, String body) {
        return WebPage.create(url, httpStatus, body, UNREACHABLE, empty(), newHashSet(), empty());
    }

    public WebPage buildTextWebPage(String url, int httpStatus, String body) {
        return WebPage.create(url, httpStatus, body, TEXT, empty(), newHashSet(), empty());
    }

    public WebPage buildBinaryWebPage(String url, int httpStatus) {
        return WebPage.create(url, httpStatus, "", BINARY, empty(), newHashSet(), empty());
    }
}

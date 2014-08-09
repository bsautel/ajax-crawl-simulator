package fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page;

import com.google.auto.value.AutoValue;

import java.util.Optional;
import java.util.Set;

@AutoValue
public abstract class WebPage {
    public abstract String getId();

    public abstract String getUrl();

    public abstract int getHttpStatus();

    public abstract String getBody();

    public abstract WebPageType getType();

    public abstract Optional<String> getTitle();

    public abstract Set<String> getLinks();

    public abstract Optional<String> getTargetUrl();

    public boolean isHtml() {
        return getType() == WebPageType.HTML;
    }

    public boolean isRedirection() {
        return getType() == WebPageType.REDIRECTION;
    }

    public boolean isUnreachable() {
        return getType() == WebPageType.UNREACHABLE;
    }

    public boolean isText() {
        return getType() == WebPageType.TEXT;
    }

    public boolean isBinary() {
        return getType() == WebPageType.BINARY;
    }

    public static WebPage create(String id, String url, int httpStatus, String body, WebPageType type,
                                 Optional<String> title, Set<String> links, Optional<String> targetUrl) {
        return new AutoValue_WebPage(id, url, httpStatus, body, type, title, links, targetUrl);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageType;

import java.util.Set;

public class JsonPage {
    private final WebPageType type;
    private final String url;
    private final int httpStatus;
    private final String body;
    private final Set<String> links;
    private String title;
    private String targetUrl;

    public JsonPage(WebPageType type, String url, int httpStatus, String body, Set<String> links) {
        this.type = type;
        this.url = url;
        this.httpStatus = httpStatus;
        this.body = body;
        this.links = links;
    }

    public String getUrl() {
        return url;
    }

    public WebPageType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public Set<String> getLinks() {
        return links;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

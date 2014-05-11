package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageType;

import java.util.Set;

public class JsonPage {
    private final WebPageType type;
    private final String url;
    private final int httpStatus;
    private final String body;
    private String title;
    private Set<String> links;
    private String targetUrl;

    public JsonPage(WebPageType type, String url, int httpStatus, String body) {
        this.type = type;
        this.url = url;
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public WebPageType getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }

    public Set<String> getLinks() {
        return links;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

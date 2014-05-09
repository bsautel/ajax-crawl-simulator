package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import java.util.Set;

public class JsonPage {
    private final String url;
    private PageType type;
    private String title;
    private String contents;
    private Set<String> links;
    private String targetUrl;
    private int httpStatus;

    public JsonPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public PageType getType() {
        return type;
    }

    public void setType(PageType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
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

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageType;

import java.util.Set;

public class MongoWebPage {
    private String simulationName;
    private WebPageType type;
    private String url;
    private int httpStatus;
    private String body;
    private String targetUrl;
    private String title;
    private Set<String> links;

    public MongoWebPage(String simulationName, WebPageType type, String url, int httpStatus, String body) {
        this.simulationName = simulationName;
        this.type = type;
        this.url = url;
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public MongoWebPage() {
    }

    public String getUrl() {
        return url;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public WebPageType getType() {
        return type;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Integer getHttpStatus() {
        return httpStatus;
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

    public void setLinks(Set<String> links) {
        this.links = links;
    }
}

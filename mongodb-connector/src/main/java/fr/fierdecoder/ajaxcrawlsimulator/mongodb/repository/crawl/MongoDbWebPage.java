package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageType;

import java.util.Set;

public class MongoDbWebPage {
    private final String simulationName;
    private final WebPageType type;
    private final String url;
    private final int httpStatus;
    private final String body;
    private final String targetUrl;
    private final String title;
    private final Set<String> links;

    @JsonCreator
    public MongoDbWebPage(@JsonProperty("simulationName") String simulationName, @JsonProperty("type") WebPageType type,
                          @JsonProperty("url") String url, @JsonProperty("httpStatus") int httpStatus,
                          @JsonProperty("body") String body, @JsonProperty("links") Set<String> links,
                          @JsonProperty("title") String title, @JsonProperty("targetUrl") String targetUrl) {
        this.simulationName = simulationName;
        this.type = type;
        this.url = url;
        this.httpStatus = httpStatus;
        this.body = body;
        this.links = links;
        this.title = title;
        this.targetUrl = targetUrl;
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

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Set<String> getLinks() {
        return links;
    }
}

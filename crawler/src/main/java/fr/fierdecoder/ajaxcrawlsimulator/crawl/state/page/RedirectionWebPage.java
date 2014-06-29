package fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page;

import java.util.Objects;

public final class RedirectionWebPage extends WebPage {
    private final String targetUrl;

    public RedirectionWebPage(String url, int httpStatus, String body, String targetUrl) {
        super(WebPageType.REDIRECTION, url, httpStatus, body);
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedirectionWebPage that = (RedirectionWebPage) o;

        return Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(getHttpStatus(), that.getHttpStatus())
                && Objects.equals(getBody(), that.getBody())
                && Objects.equals(targetUrl, that.targetUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getHttpStatus(), getBody(), targetUrl);
    }

    @Override
    public String toString() {
        return "RedirectionWebPage{" +
                "url='" + getUrl() + '\'' +
                "httpStatus='" + getHttpStatus() + '\'' +
                "body='" + getBody() + '\'' +
                "targetUrl='" + targetUrl + '\'' +
                '}';
    }
}

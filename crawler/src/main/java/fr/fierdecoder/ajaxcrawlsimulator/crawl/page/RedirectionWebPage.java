package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import java.util.Objects;

public final class RedirectionWebPage extends WebPage {
    private final String targetUrl;

    public RedirectionWebPage(String url, String targetUrl) {
        super(url);
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
                && Objects.equals(targetUrl, that.targetUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), targetUrl);
    }

    @Override
    public String toString() {
        return "RedirectionWebPage{" +
                "url='" + getUrl() + '\'' +
                "targetUrl='" + targetUrl + '\'' +
                '}';
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import java.util.Objects;

public final class UnreachableWebPage extends WebPage {
    private final int httpStatus;

    public UnreachableWebPage(String url, int httpStatus) {
        super(url);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnreachableWebPage that = (UnreachableWebPage) o;

        return Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(httpStatus, that.httpStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), httpStatus);
    }

    @Override
    public String toString() {
        return "UnreachableWebPage{" +
                "url=" + getUrl() +
                "httpStatus=" + httpStatus +
                '}';
    }
}

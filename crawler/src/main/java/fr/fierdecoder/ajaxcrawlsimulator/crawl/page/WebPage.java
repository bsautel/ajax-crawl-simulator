package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import java.util.Objects;

import static fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageType.*;

public class WebPage {
    private final WebPageType type;
    private final String url;
    private final int httpStatus;
    private final String body;

    public WebPage(WebPageType type, String url, int httpStatus, String body) {
        this.type = type;
        this.url = url;
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getBody() {
        return body;
    }

    public boolean isHtmlWebPage() {
        return type == HTML;
    }

    public HtmlWebPage asHtmlWebPage() {
        return (HtmlWebPage) this;
    }

    public boolean isRedirection() {
        return type == REDIRECTION;
    }

    public RedirectionWebPage asRedirection() {
        return (RedirectionWebPage) this;
    }

    public boolean isUnreachableWebPage() {
        return type == UNREACHABLE;
    }

    public WebPageType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPage webPage = (WebPage) o;

        return Objects.equals(type, webPage.type)
                && Objects.equals(httpStatus, webPage.httpStatus)
                && Objects.equals(body, webPage.body)
                && Objects.equals(type, webPage.type)
                && Objects.equals(url, webPage.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url, httpStatus, body);
    }

    @Override
    public String toString() {
        return "WebPage{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", httpStatus=" + httpStatus +
                ", body='" + body + '\'' +
                '}';
    }
}

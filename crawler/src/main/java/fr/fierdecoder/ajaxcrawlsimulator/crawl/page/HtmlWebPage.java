package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import java.util.Objects;
import java.util.Set;

import static fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageType.HTML;

public final class HtmlWebPage extends WebPage {
    private final String title;
    private final Set<String> links;

    public HtmlWebPage(String url, int httpStatus, String title, String body, Set<String> links) {
        super(HTML, url, httpStatus, body);
        this.title = title;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getLinks() {
        return links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HtmlWebPage that = (HtmlWebPage) o;
        return Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(getHttpStatus(), that.getHttpStatus())
                && Objects.equals(title, that.getTitle())
                && Objects.equals(getBody(), that.getBody())
                && Objects.equals(links, that.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getHttpStatus(), title, getBody(), links);
    }

    @Override
    public String toString() {
        return "HtmlWebPage{" +
                "url='" + getUrl() + '\'' +
                "httpStatus='" + getHttpStatus() + '\'' +
                "title='" + title + '\'' +
                ", body='" + getBody() + '\'' +
                ", links=" + links +
                '}';
    }
}

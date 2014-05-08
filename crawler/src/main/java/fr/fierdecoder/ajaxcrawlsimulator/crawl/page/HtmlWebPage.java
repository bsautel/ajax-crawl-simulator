package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import java.util.Objects;
import java.util.Set;

public final class HtmlWebPage extends WebPage {
    private final String title;
    private final String contents;
    private final Set<String> links;

    public HtmlWebPage(String url, String title, String contents, Set<String> links) {
        super(url);
        this.title = title;
        this.contents = contents;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
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
                && Objects.equals(title, that.getTitle())
                && Objects.equals(contents, that.getContents())
                && Objects.equals(links, that.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), title, contents, links);
    }

    @Override
    public String toString() {
        return "HtmlWebPage{" +
                "url='" + getUrl() + '\'' +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", links=" + links +
                '}';
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

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
}

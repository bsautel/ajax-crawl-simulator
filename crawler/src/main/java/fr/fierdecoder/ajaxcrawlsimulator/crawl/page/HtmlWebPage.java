package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import java.util.Set;

public final class HtmlWebPage extends WebPage {
    private final String contents;
    private final Set<String> links;

    public HtmlWebPage(String url, String contents, Set<String> links) {
        super(url);
        this.contents = contents;
        this.links = links;
    }

    public String getContents() {
        return contents;
    }

    public Set<String> getLinks() {
        return links;
    }
}

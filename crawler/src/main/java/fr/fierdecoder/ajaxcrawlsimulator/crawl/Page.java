package fr.fierdecoder.ajaxcrawlsimulator.crawl;

public class Page {
    private final String url;
    private final String contents;

    protected Page(String contents, String url) {
        this.contents = contents;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getContents() {
        return contents;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

public final class UnreachableWebPage extends WebPage {
    private final int httpStatus;

    public UnreachableWebPage(String url, int httpStatus) {
        super(url);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

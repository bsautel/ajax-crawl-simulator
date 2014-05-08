package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

public final class RedirectionWebPage extends WebPage {
    private final String targetUrl;

    public RedirectionWebPage(String url, String targetUrl) {
        super(url);
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }
}

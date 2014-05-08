package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

public abstract class WebPage {
    private final String url;

    public WebPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHtmlWebPage() {
        return this instanceof HtmlWebPage;
    }

    public HtmlWebPage asHtmlWebPage() {
        return (HtmlWebPage) this;
    }

    public boolean isRedirection() {
        return this instanceof RedirectionWebPage;
    }

    public RedirectionWebPage asRedirection() {
        return (RedirectionWebPage) this;
    }

    public boolean isUnreachableWebPage() {
        return this instanceof UnreachableWebPage;
    }

    public UnreachableWebPage asUnreachableWebPage() {
        return (UnreachableWebPage) this;
    }
}

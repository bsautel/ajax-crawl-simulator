package fr.fierdecoder.ajaxcrawlsimulator.web.value;


import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageType;

public class JsonPagePreview {
    private final WebPageType type;
    private final String url;
    private String title;

    public JsonPagePreview(WebPageType type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WebPageType getType() {
        return type;
    }
}

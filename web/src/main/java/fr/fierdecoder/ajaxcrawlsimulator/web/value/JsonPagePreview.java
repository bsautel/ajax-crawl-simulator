package fr.fierdecoder.ajaxcrawlsimulator.web.value;

public class JsonPagePreview {
    private final String url;
    private PageType type;
    private String title;

    public JsonPagePreview(String url) {
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

    public PageType getType() {
        return type;
    }

    public void setType(PageType type) {
        this.type = type;
    }
}

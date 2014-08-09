package fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page;

import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
public abstract class WebPagePreview {
    public abstract String getId();

    public abstract WebPageType getType();

    public abstract String getUrl();

    public abstract Optional<String> getTitle();

    public static WebPagePreview create(String id, WebPageType type, String url, Optional<String> title) {
        return new AutoValue_WebPagePreview(id, type, url, title);
    }
}

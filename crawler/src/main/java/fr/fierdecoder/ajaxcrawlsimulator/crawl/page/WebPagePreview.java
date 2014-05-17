package fr.fierdecoder.ajaxcrawlsimulator.crawl.page;

import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
public abstract class WebPagePreview {
    public abstract WebPageType getType();

    public abstract String getUrl();

    public abstract Optional<String> getTitle();

    public static WebPagePreview create(WebPageType type, String url, Optional<String> title) {
        return new AutoValue_WebPagePreview(type, url, title);
    }
}

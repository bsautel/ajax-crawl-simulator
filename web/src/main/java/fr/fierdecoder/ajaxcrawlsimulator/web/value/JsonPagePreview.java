package fr.fierdecoder.ajaxcrawlsimulator.web.value;


import com.google.auto.value.AutoValue;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageType;

import java.util.Optional;

@AutoValue
public abstract class JsonPagePreview {
    public abstract String getUrl();

    public abstract WebPageType getType();

    public abstract String getTitle();

    public static JsonPagePreview create(String url, WebPageType type, Optional<String> title) {
        return new AutoValue_JsonPagePreview(url, type, title.orElse(""));
    }
}

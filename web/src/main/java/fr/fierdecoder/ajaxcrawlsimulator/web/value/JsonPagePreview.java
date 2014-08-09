package fr.fierdecoder.ajaxcrawlsimulator.web.value;


import com.google.auto.value.AutoValue;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageType;

import java.util.Optional;

@AutoValue
public abstract class JsonPagePreview {
    public abstract String getId();

    public abstract String getUrl();

    public abstract WebPageType getType();

    public abstract String getTitle();

    public static JsonPagePreview create(String id, String url, WebPageType type, Optional<String> title) {
        return new AutoValue_JsonPagePreview(id, url, type, title.orElse(""));
    }
}

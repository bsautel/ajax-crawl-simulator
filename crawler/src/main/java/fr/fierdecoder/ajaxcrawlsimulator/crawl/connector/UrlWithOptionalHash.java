package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import com.google.auto.value.AutoValue;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@AutoValue
public abstract class UrlWithOptionalHash {
    public abstract String getUrlWithoutHash();

    protected abstract Optional<String> getOptionalHash();


    public boolean hasFragment() {
        return getOptionalHash()
                .map(hash -> hash.startsWith("!"))
                .orElse(false);
    }

    public String getFragment() {
        String hash = getOptionalHash().get();
        return hash.replaceFirst("!", "");
    }

    public boolean hasHash() {
        return getOptionalHash().isPresent();
    }

    public String getHash() {
        return getOptionalHash().get();
    }

    public static UrlWithOptionalHash create(String url) {
        if (url.contains("#")) {
            String[] splitUrl = url.split("#");
            String urlWithoutHash = splitUrl[0];
            if (splitUrl.length > 1) {
                return new AutoValue_UrlWithOptionalHash(urlWithoutHash, of(splitUrl[1]));
            }
            return new AutoValue_UrlWithOptionalHash(urlWithoutHash, of(""));
        }
        return new AutoValue_UrlWithOptionalHash(url, empty());
    }
}

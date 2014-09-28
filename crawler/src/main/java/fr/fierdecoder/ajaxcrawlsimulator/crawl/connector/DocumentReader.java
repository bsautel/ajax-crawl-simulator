package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

public class DocumentReader {
    public Set<String> readLinks(Document document) {
        Elements pageLinksElements = document.select("a");
        return pageLinksElements.stream()
                .map(linkElement -> linkElement.attr("abs:href"))
                .filter(url -> !url.isEmpty())
                .collect(toSet());
    }

    public String readTitle(Document document) {
        return document.title();
    }

    public Optional<String> readCanonicalUrl(Document document) {
        Elements canonicalUrlElements = document.select("link[rel=canonical]");
        if (!canonicalUrlElements.isEmpty()) {
            Element canonicalUrlElement = canonicalUrlElements.get(0);
            return ofNullable(canonicalUrlElement.attr("abs:href"));
        }
        return empty();
    }

    public boolean supportsFragment(Document document) {
        Elements fragmentMeta = document.select("meta[name=fragment]");
        return fragmentMeta.size() == 1;
    }
}

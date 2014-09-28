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
    private final Document document;

    public DocumentReader(Document document) {
        this.document = document;
    }

    public Set<String> readLinks() {
        Elements pageLinksElements = document.select("a");
        return pageLinksElements.stream()
                .map(linkElement -> linkElement.attr("abs:href"))
                .filter(url -> !url.isEmpty())
                .collect(toSet());
    }

    public String readTitle() {
        return document.title();
    }

    public Optional<String> readCanonicalUrl() {
        Elements canonicalUrlElements = document.select("link[rel=canonical]");
        if (!canonicalUrlElements.isEmpty()) {
            Element canonicalUrlElement = canonicalUrlElements.get(0);
            return ofNullable(canonicalUrlElement.attr("abs:href"));
        }
        return empty();
    }

    public boolean supportsFragment() {
        Elements fragmentMeta = document.select("meta[name=fragment]");
        return fragmentMeta.size() == 1;
    }
}

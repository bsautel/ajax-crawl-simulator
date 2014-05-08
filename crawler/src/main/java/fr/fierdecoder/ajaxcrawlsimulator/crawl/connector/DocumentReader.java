package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Set;

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
}

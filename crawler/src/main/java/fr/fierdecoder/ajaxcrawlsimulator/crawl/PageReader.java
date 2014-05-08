package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.UnreachableWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Singleton
public class PageReader {
    public WebPage readPage(String url) {
        try {
            return fetchPage(url);
        } catch (IOException e) {
            return new UnreachableWebPage(url, 0);
        }
    }

    private WebPage fetchPage(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        /*Elements fragmentMeta = document.select("meta[name=fragment]");
        if (fragmentMeta.size() == 1) {
            String fragmentMetaValue = fragmentMeta.get(0).attr("content");
            return readPage(url.replace("#", "?_escaped_fragment_="));
        }*/

        Set<String> links = readLinks(document);
        return new HtmlWebPage(url, document.html(), links);
    }

    private Set<String> readLinks(Document document) {
        Elements pageLinksElements = document.select("a");
        return pageLinksElements.stream()
                .map(linkElement -> linkElement.attr("abs:href"))
                .filter(url -> !url.isEmpty())
                .collect(toSet());
    }
}

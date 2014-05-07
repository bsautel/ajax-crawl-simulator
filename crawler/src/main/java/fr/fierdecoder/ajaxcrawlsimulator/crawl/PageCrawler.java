package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PageCrawler {
    public Page fetchPage(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        /*Elements fragmentMeta = document.select("meta[name=fragment]");
        if (fragmentMeta.size() == 1) {
            String fragmentMetaValue = fragmentMeta.get(0).attr("content");
            return fetchPage(url.replace("#", "?_escaped_fragment_="));
        }*/
        return new Page(url, document.html());
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.UnreachableWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Set;

@Singleton
public class NetworkPageReader implements PageReader {
    private final DocumentReader documentReader;

    @Inject
    public NetworkPageReader(DocumentReader documentReader) {
        this.documentReader = documentReader;
    }

    @Override
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

        Set<String> links = documentReader.readLinks(document);
        String title = documentReader.readTitle(document);
        return new HtmlWebPage(url, title, document.html(), links);
    }
}

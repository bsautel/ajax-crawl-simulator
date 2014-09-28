package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

import java.util.Optional;

public class WebPagePreviewConverter {
    public static WebPagePreview createWebPagePreview(WebPage webPage) {
        return WebPagePreview.create(webPage.getType(), webPage.getUrl(), webPage.getTitle());
    }
}

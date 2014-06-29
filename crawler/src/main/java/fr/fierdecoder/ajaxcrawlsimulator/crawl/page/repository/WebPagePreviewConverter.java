package fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPagePreview;

import java.util.Optional;

public class WebPagePreviewConverter {
    public static WebPagePreview createWebPagePreview(WebPage webPage) {
        Optional<String> title = webPage.isHtml() ? Optional.of(webPage.asHtml().getTitle()) : Optional.<String>empty();
        return WebPagePreview.create(webPage.getType(), webPage.getUrl(), title);
    }
}

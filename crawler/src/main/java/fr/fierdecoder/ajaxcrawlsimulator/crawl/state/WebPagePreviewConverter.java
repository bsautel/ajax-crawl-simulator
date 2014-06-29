package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

import java.util.Optional;

public class WebPagePreviewConverter {
    public static WebPagePreview createWebPagePreview(WebPage webPage) {
        Optional<String> title = webPage.isHtml() ? Optional.of(webPage.asHtml().getTitle()) : Optional.<String>empty();
        return WebPagePreview.create(webPage.getType(), webPage.getUrl(), title);
    }
}

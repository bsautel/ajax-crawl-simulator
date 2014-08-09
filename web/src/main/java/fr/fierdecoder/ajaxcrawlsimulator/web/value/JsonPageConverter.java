package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

@Singleton
public class JsonPageConverter {
    public JsonPage createJsonPage(WebPage webPage) {
        JsonPage jsonPage = new JsonPage(webPage.getType(), webPage.getUrl(),
                webPage.getHttpStatus(), webPage.getBody(), webPage.getLinks());
        if (webPage.getTitle().isPresent()) {
            jsonPage.setTitle(webPage.getTitle().get());
        }
        if (webPage.getTargetUrl().isPresent()) {
            jsonPage.setTargetUrl(webPage.getTargetUrl().get());
        }
        return jsonPage;
    }

    public JsonPagePreview createJsonPagePreview(WebPagePreview webPage) {
        return JsonPagePreview.create(webPage.getId(), webPage.getUrl(), webPage.getType(), webPage.getTitle());
    }
}

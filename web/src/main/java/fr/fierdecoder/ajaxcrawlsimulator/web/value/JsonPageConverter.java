package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

@Singleton
public class JsonPageConverter {
    public JsonPage createJsonPage(WebPage webPage) {
        JsonPage jsonPage = new JsonPage(webPage.getType(), webPage.getUrl(),
                webPage.getHttpStatus(), webPage.getBody());
        if (webPage.isHtml()) {
            HtmlWebPage htmlPage = webPage.asHtml();
            jsonPage.setTitle(htmlPage.getTitle());
            jsonPage.setLinks(htmlPage.getLinks());
        } else if (webPage.isRedirection()) {
            RedirectionWebPage redirectionPage = webPage.asRedirection();
            jsonPage.setTargetUrl(redirectionPage.getTargetUrl());
        }
        return jsonPage;
    }

    public JsonPagePreview createJsonPagePreview(WebPagePreview webPage) {
        return JsonPagePreview.create(webPage.getUrl(), webPage.getType(), webPage.getTitle());
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

@Singleton
public class JsonPageConverter {
    public JsonPage createJsonPage(WebPage webPage) {
        JsonPage jsonPage = new JsonPage(webPage.getType(), webPage.getUrl(),
                webPage.getHttpStatus(), webPage.getBody());
        if (webPage.isHtmlWebPage()) {
            HtmlWebPage htmlPage = webPage.asHtmlWebPage();
            jsonPage.setTitle(htmlPage.getTitle());
            jsonPage.setLinks(htmlPage.getLinks());
        } else if (webPage.isRedirection()) {
            RedirectionWebPage redirectionPage = webPage.asRedirection();
            jsonPage.setTargetUrl(redirectionPage.getTargetUrl());
        }
        return jsonPage;
    }

    public JsonPagePreview createJsonPagePreview(WebPage webPage) {
        JsonPagePreview jsonPage = new JsonPagePreview(webPage.getType(), webPage.getUrl());
        if (webPage.isHtmlWebPage()) {
            HtmlWebPage htmlPage = webPage.asHtmlWebPage();
            jsonPage.setTitle(htmlPage.getTitle());
        }
        return jsonPage;
    }
}

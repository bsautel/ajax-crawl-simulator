package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.*;

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
        JsonPagePreview jsonPage = new JsonPagePreview(webPage.getType(), webPage.getUrl());
        if (webPage.getTitle().isPresent()) {
            jsonPage.setTitle(webPage.getTitle().get());
        }
        return jsonPage;
    }
}

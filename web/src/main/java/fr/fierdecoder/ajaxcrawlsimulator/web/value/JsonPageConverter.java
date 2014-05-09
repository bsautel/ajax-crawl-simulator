package fr.fierdecoder.ajaxcrawlsimulator.web.value;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.UnreachableWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import static fr.fierdecoder.ajaxcrawlsimulator.web.value.PageType.HTML;
import static fr.fierdecoder.ajaxcrawlsimulator.web.value.PageType.REDIRECTION;
import static fr.fierdecoder.ajaxcrawlsimulator.web.value.PageType.UNREACHABLE;

@Singleton
public class JsonPageConverter {
    public JsonPage createJsonPage(WebPage webPage) {
        JsonPage jsonPage = new JsonPage(webPage.getUrl());
        if(webPage.isHtmlWebPage()) {
            jsonPage.setType(HTML);
            HtmlWebPage htmlPage = webPage.asHtmlWebPage();
            jsonPage.setTitle(htmlPage.getTitle());
            jsonPage.setContents(htmlPage.getTitle());
            jsonPage.setLinks(htmlPage.getLinks());
        } else if (webPage.isRedirection()) {
            jsonPage.setType(REDIRECTION);
            RedirectionWebPage redirectionPage = webPage.asRedirection();
            jsonPage.setTargetUrl(redirectionPage.getTargetUrl());
        } else if (webPage.isUnreachableWebPage()) {
            jsonPage.setType(UNREACHABLE);
            UnreachableWebPage unreachable = webPage.asUnreachableWebPage();
            jsonPage.setHttpStatus(unreachable.getHttpStatus());
        }
        return jsonPage;
    }

    public JsonPagePreview createJsonPagePreview(WebPage webPage) {
        JsonPagePreview jsonPage = new JsonPagePreview(webPage.getUrl());
        if(webPage.isHtmlWebPage()) {
            jsonPage.setType(HTML);
            HtmlWebPage htmlPage = webPage.asHtmlWebPage();
            jsonPage.setTitle(htmlPage.getTitle());
        } else if (webPage.isRedirection()) {
            jsonPage.setType(REDIRECTION);
        } else if (webPage.isUnreachableWebPage()) {
            jsonPage.setType(UNREACHABLE);
        }
        return jsonPage;
    }
}

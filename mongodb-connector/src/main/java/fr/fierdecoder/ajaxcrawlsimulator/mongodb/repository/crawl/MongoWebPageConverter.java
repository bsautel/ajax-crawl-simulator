package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageFactory;

@Singleton
public class MongoWebPageConverter {
    private final WebPageFactory webPageFactory;

    public MongoWebPageConverter(WebPageFactory webPageFactory) {
        this.webPageFactory = webPageFactory;
    }

    public MongoWebPage convertToMongo(WebPage webPage, String simulationName) {
        MongoWebPage result = new MongoWebPage(simulationName, webPage.getType(),
                webPage.getUrl(), webPage.getHttpStatus(), webPage.getBody());
        if (webPage.isHtml()) {
            HtmlWebPage htmlWebPage = webPage.asHtml();
            result.setTitle(htmlWebPage.getTitle());
            result.setLinks(htmlWebPage.getLinks());
        } else if (webPage.isRedirection()) {
            RedirectionWebPage redirectionWebPage = webPage.asRedirection();
            result.setTargetUrl(redirectionWebPage.getTargetUrl());
        }
        return result;
    }

    public WebPage convertFromMongo(MongoWebPage mongoPage) {
        switch (mongoPage.getType()) {
            case HTML:
                return webPageFactory.buildHtmlWebPage(mongoPage.getUrl(), mongoPage.getHttpStatus(),
                        mongoPage.getTitle(), mongoPage.getBody(), mongoPage.getLinks());
            case REDIRECTION:
                return webPageFactory.buildRedirectionWebPage(mongoPage.getUrl(), mongoPage.getHttpStatus(),
                        mongoPage.getBody(), mongoPage.getTargetUrl());
            case UNREACHABLE:
                return webPageFactory.buildUnreachableWebPage(mongoPage.getUrl(), mongoPage.getHttpStatus(),
                        mongoPage.getBody());
            case TEXT:
                return webPageFactory.buildTextWebPage(mongoPage.getUrl(), mongoPage.getHttpStatus(),
                        mongoPage.getBody());
            case BINARY:
                return webPageFactory.buildBinaryWebPage(mongoPage.getUrl(), mongoPage.getHttpStatus());
        }
        throw new IllegalArgumentException();
    }
}
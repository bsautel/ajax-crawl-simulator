package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.UnreachableWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import static fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoWebPage.Type.*;

public class MongoWebPageConverter {
    public MongoWebPage convertToMongo(WebPage webPage, String simulatioName) {
        MongoWebPage result = new MongoWebPage();
        result.setUrl(webPage.getUrl());
        result.setSimulationName(simulatioName);
        if (webPage.isHtmlWebPage()) {
            result.setType(HTML);
            HtmlWebPage htmlWebPage  = webPage.asHtmlWebPage();
            result.setTitle(htmlWebPage.getTitle());
            result.setContents(htmlWebPage.getContents());
            result.setLinks(htmlWebPage.getLinks());
        }
        else if (webPage.isRedirection()) {
            result.setType(REDIRECTION);
            RedirectionWebPage redirectionWebPage = webPage.asRedirection();
            result.setTargetUrl(redirectionWebPage.getTargetUrl());
        }
        else if (webPage.isUnreachableWebPage()) {
            result.setType(UNREACHABLE);
            UnreachableWebPage unreachableWebPage = webPage.asUnreachableWebPage();
            result.setHttpStatus(unreachableWebPage.getHttpStatus());
        }
        return result;
    }

    public WebPage convertFromMongo(MongoWebPage mongoPage) {
        switch(mongoPage.getType()) {
            case HTML:
                return new HtmlWebPage(mongoPage.getUrl(), mongoPage.getTitle(),
                        mongoPage.getContents(), mongoPage.getLinks());
            case REDIRECTION:
                return new RedirectionWebPage(mongoPage.getUrl(), mongoPage.getTargetUrl());
            case UNREACHABLE:
                return new UnreachableWebPage(mongoPage.getUrl(), mongoPage.getHttpStatus());
        }
        throw new IllegalArgumentException();
    }
}

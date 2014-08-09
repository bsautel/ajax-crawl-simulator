package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;

import static java.util.Optional.ofNullable;

@Singleton
public class MongoDbWebPageConverter {
    public MongoDbWebPage convertToMongo(WebPage webPage, String simulationName) {
        return new MongoDbWebPage(webPage.getId(), simulationName, webPage.getType(),
                webPage.getUrl(), webPage.getHttpStatus(), webPage.getBody(), webPage.getLinks(),
                webPage.getTitle().orElse(null), webPage.getTargetUrl().orElse(null));
    }

    public WebPage convertFromMongo(MongoDbWebPage mongoPage) {
        return WebPage.create(mongoPage.getId(), mongoPage.getUrl(), mongoPage.getHttpStatus(), mongoPage.getBody(),
                mongoPage.getType(), ofNullable(mongoPage.getTitle()), mongoPage.getLinks(),
                ofNullable(mongoPage.getTargetUrl()));
    }

    public WebPagePreview convertToPreview(MongoDbWebPage mongoWebPage) {
        return WebPagePreview.create(mongoWebPage.getId(), mongoWebPage.getType(), mongoWebPage.getUrl(),
                ofNullable(mongoWebPage.getTitle()));
    }
}

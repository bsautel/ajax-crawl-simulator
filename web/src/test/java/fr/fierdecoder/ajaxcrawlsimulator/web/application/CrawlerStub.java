package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageFactory;

import static com.google.common.collect.Sets.newHashSet;


public class CrawlerStub implements Crawler {
    public static final String PAGE_TITLE = "Page title";
    public static final String PAGE_BODY = "Page body";
    private static final String BASE_URL = "http://domain";
    public static final String HOME_URL = BASE_URL + "/";
    public static final String CONTACT_URL = BASE_URL + "/contact";
    public static final String ABOUT_URL = BASE_URL + "/about";

    @Override
    public void crawl(CrawlPerimeter crawlPerimeter, CrawlState state) {
        WebPageFactory webPageFactory = new WebPageFactory();
        WebPage htmlWebPage = webPageFactory.buildHtmlWebPage(ABOUT_URL, 200, PAGE_TITLE, PAGE_BODY,
                newHashSet(CONTACT_URL));
        state.addPage(htmlWebPage);
        WebPage redirectionWebPage = webPageFactory.buildRedirectionWebPage(HOME_URL, 301, "", CONTACT_URL);
        state.addPage(redirectionWebPage);
        state.addPage(webPageFactory.buildUnreachableWebPage(CONTACT_URL, 404, ""));
    }
}

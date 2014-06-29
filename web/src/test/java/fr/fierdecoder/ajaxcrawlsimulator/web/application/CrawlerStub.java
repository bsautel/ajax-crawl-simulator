package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepository;

import static com.google.common.collect.Sets.newHashSet;


public class CrawlerStub implements Crawler {
    private static final String BASE_URL = "http://domain";
    public static final String HOME_URL = BASE_URL + "/";
    public static final String CONTACT_URL = BASE_URL + "/contact";
    public static final String ABOUT_URL = BASE_URL + "/about";
    public static final String PAGE_TITLE = "Page title";
    public static final String PAGE_BODY = "Page body";

    @Override
    public void crawl(CrawlPerimeter crawlPerimeter, WebPagesRepository repository) {
        WebPageFactory webPageFactory = new WebPageFactory();
        HtmlWebPage htmlWebPage = webPageFactory.buildHtmlWebPage(ABOUT_URL, 200, PAGE_TITLE, PAGE_BODY,
                newHashSet(CONTACT_URL));
        repository.add(htmlWebPage);
        RedirectionWebPage redirectionWebPage = webPageFactory.buildRedirectionWebPage(HOME_URL, 301, "", CONTACT_URL);
        repository.add(redirectionWebPage);
        repository.add(webPageFactory.buildUnreachableWebPage(CONTACT_URL, 404, ""));
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrawlerTest {
    private static final String INDEX_URL = "http://mydomain.com/";
    private static final String HOME_URL = INDEX_URL + "home";
    private static final String CONTACT_URL = INDEX_URL + "contact";
    public static final String HTML_CONTENTS = "This is HTML contents";

    @Mock
    private PageReader pageReader;

    @Test
    public void singlePageCrawl() {
        CrawlPerimeter crawlPerimeter = new CrawlPerimeter(INDEX_URL);
        HtmlWebPage indexPage = registerWebPage(new HtmlWebPage(INDEX_URL, HTML_CONTENTS, newHashSet()));
        Crawler crawler = new Crawler(crawlPerimeter, pageReader);

        WebPagesRegistry result = crawler.crawl();

        assertEquals(1, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
    }

    private <PageType extends WebPage> PageType registerWebPage(PageType webPage) {
        String url = webPage.getUrl();
        when(pageReader.readPage(url)).thenReturn(webPage);
        return webPage;
    }

    @Test
    public void twoPagesCrawl() {
        CrawlPerimeter crawlPerimeter = new CrawlPerimeter(INDEX_URL);
        HtmlWebPage indexPage = registerWebPage(new HtmlWebPage(INDEX_URL, HTML_CONTENTS, newHashSet(CONTACT_URL)));
        HtmlWebPage contactPage = registerWebPage(new HtmlWebPage(CONTACT_URL, HTML_CONTENTS, newHashSet()));
        Crawler crawler = new Crawler(crawlPerimeter, pageReader);

        WebPagesRegistry result = crawler.crawl();

        assertEquals(2, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
        assertEquals(contactPage, result.getByUrl(CONTACT_URL));
    }

    @Test
    public void twoCyclingPagesCrawl() {
        CrawlPerimeter crawlPerimeter = new CrawlPerimeter(INDEX_URL);
        HtmlWebPage indexPage = registerWebPage(new HtmlWebPage(INDEX_URL, HTML_CONTENTS, newHashSet(CONTACT_URL)));
        HtmlWebPage contactPage = registerWebPage(new HtmlWebPage(CONTACT_URL, HTML_CONTENTS, newHashSet(INDEX_URL)));
        Crawler crawler = new Crawler(crawlPerimeter, pageReader);

        WebPagesRegistry result = crawler.crawl();

        assertEquals(2, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
        assertEquals(contactPage, result.getByUrl(CONTACT_URL));
    }

    @Test
    public void redirectionPage() {
        CrawlPerimeter crawlPerimeter = new CrawlPerimeter(INDEX_URL);
        RedirectionWebPage indexPage = registerWebPage(new RedirectionWebPage(INDEX_URL, HOME_URL));
        HtmlWebPage homePage = registerWebPage(new HtmlWebPage(HOME_URL, HTML_CONTENTS, newHashSet()));
        Crawler crawler = new Crawler(crawlPerimeter, pageReader);

        WebPagesRegistry result = crawler.crawl();

        assertEquals(2, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
        assertEquals(homePage, result.getByUrl(HOME_URL));
    }
}

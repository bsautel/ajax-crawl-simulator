package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrawlerTest {
    private static final String INDEX_URL = "http://mydomain.com/";
    private static final String HOME_URL = INDEX_URL + "home";
    private static final String CONTACT_URL = INDEX_URL + "contact";
    private static final String PAGE_TITLE = "This is the page title";
    private static final String HTML_CONTENTS = "This is HTML contents";

    @Mock
    private PageReader pageReader;
    @Mock
    private CrawlPerimeter crawlPerimeter;

    @Before
    public void setUp() {
        when(crawlPerimeter.getEntryUrl()).thenReturn(INDEX_URL);
        when(crawlPerimeter.contains(anyString())).thenReturn(true);
    }

    @Test
    public void singlePageCrawl() {
        HtmlWebPage indexPage = registerWebPage(new HtmlWebPage(INDEX_URL, PAGE_TITLE, HTML_CONTENTS, newHashSet()));
        Crawler crawler = new Crawler(pageReader);

        WebPagesRegistry result = crawler.crawl(crawlPerimeter);

        assertEquals(1, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
    }

    private HtmlWebPage buildHtmlWebPage(String url, String... links) {
        return new HtmlWebPage(url, PAGE_TITLE, HTML_CONTENTS, newHashSet(links));
    }

    private <PageType extends WebPage> PageType registerWebPage(PageType webPage) {
        String url = webPage.getUrl();
        when(pageReader.readPage(url)).thenReturn(webPage);
        return webPage;
    }

    @Test
    public void twoPagesCrawl() {
        HtmlWebPage indexPage = registerWebPage(buildHtmlWebPage(INDEX_URL, CONTACT_URL));
        HtmlWebPage contactPage = registerWebPage(buildHtmlWebPage(CONTACT_URL));
        Crawler crawler = new Crawler(pageReader);

        WebPagesRegistry result = crawler.crawl(crawlPerimeter);

        assertEquals(2, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
        assertEquals(contactPage, result.getByUrl(CONTACT_URL));
    }

    @Test
    public void twoCyclingPagesCrawl() {
        HtmlWebPage indexPage = registerWebPage(buildHtmlWebPage(INDEX_URL, CONTACT_URL));
        HtmlWebPage contactPage = registerWebPage(buildHtmlWebPage(CONTACT_URL, INDEX_URL));
        Crawler crawler = new Crawler(pageReader);

        WebPagesRegistry result = crawler.crawl(crawlPerimeter);

        assertEquals(2, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
        assertEquals(contactPage, result.getByUrl(CONTACT_URL));
    }

    @Test
    public void twoPagesIncludingOneIgnored() {
        HtmlWebPage indexPage = registerWebPage(buildHtmlWebPage(INDEX_URL, CONTACT_URL));
        when(crawlPerimeter.contains(CONTACT_URL)).thenReturn(false);
        Crawler crawler = new Crawler(pageReader);

        WebPagesRegistry result = crawler.crawl(crawlPerimeter);

        assertEquals(1, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
    }

    @Test
    public void redirectionPage() {
        RedirectionWebPage indexPage = registerWebPage(new RedirectionWebPage(INDEX_URL, HOME_URL));
        HtmlWebPage homePage = registerWebPage(buildHtmlWebPage(HOME_URL));
        Crawler crawler = new Crawler(pageReader);

        WebPagesRegistry result = crawler.crawl(crawlPerimeter);

        assertEquals(2, result.getPagesCount());
        assertEquals(indexPage, result.getByUrl(INDEX_URL));
        assertEquals(homePage, result.getByUrl(HOME_URL));
    }
}

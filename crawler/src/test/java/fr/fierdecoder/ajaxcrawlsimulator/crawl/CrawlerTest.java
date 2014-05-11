package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.MemoryWebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private WebPageFactory webPageFactory;
    private WebPagesRegistry registry;

    @Before
    public void setUp() {
        webPageFactory = new WebPageFactory();
        registry = new MemoryWebPagesRegistry();
        when(crawlPerimeter.getEntryUrl()).thenReturn(INDEX_URL);
        when(crawlPerimeter.contains(anyString())).thenReturn(true);
    }

    @Test
    public void singlePageCrawl() {
        HtmlWebPage indexPage = registerWebPage(buildHtmlWebPage(INDEX_URL));
        Crawler crawler = new Crawler(pageReader);

        crawler.crawl(crawlPerimeter, registry);

        assertEquals(1, registry.getPagesCount());
        assertThat(registry.getByUrl(INDEX_URL).get(), is(indexPage));
    }

    private HtmlWebPage buildHtmlWebPage(String url, String... links) {
        return webPageFactory.buildHtmlWebPage(url, 200, PAGE_TITLE, HTML_CONTENTS, newHashSet(links));
    }

    private RedirectionWebPage buildRedirectionWebPage(String url, String targetUrl) {
        return webPageFactory.buildRedirectionWebPage(url, 301, "", targetUrl);
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

        crawler.crawl(crawlPerimeter, registry);

        assertEquals(2, registry.getPagesCount());
        assertThat(registry.getByUrl(INDEX_URL).get(), is(indexPage));
        assertThat(registry.getByUrl(CONTACT_URL).get(), is(contactPage));
    }

    @Test
    public void twoCyclingPagesCrawl() {
        HtmlWebPage indexPage = registerWebPage(buildHtmlWebPage(INDEX_URL, CONTACT_URL));
        HtmlWebPage contactPage = registerWebPage(buildHtmlWebPage(CONTACT_URL, INDEX_URL));
        Crawler crawler = new Crawler(pageReader);

        crawler.crawl(crawlPerimeter, registry);

        assertEquals(2, registry.getPagesCount());
        assertThat(registry.getByUrl(INDEX_URL).get(), is(indexPage));
        assertThat(registry.getByUrl(CONTACT_URL).get(), is(contactPage));
    }

    @Test
    public void twoPagesIncludingOneIgnored() {
        HtmlWebPage indexPage = registerWebPage(buildHtmlWebPage(INDEX_URL, CONTACT_URL));
        when(crawlPerimeter.contains(CONTACT_URL)).thenReturn(false);
        Crawler crawler = new Crawler(pageReader);

        crawler.crawl(crawlPerimeter, registry);

        assertEquals(1, registry.getPagesCount());
        assertThat(registry.getByUrl(INDEX_URL).get(), is(indexPage));
    }

    @Test
    public void redirectionPage() {
        RedirectionWebPage indexPage = registerWebPage(buildRedirectionWebPage(INDEX_URL, HOME_URL));
        HtmlWebPage homePage = registerWebPage(buildHtmlWebPage(HOME_URL));
        Crawler crawler = new Crawler(pageReader);

        crawler.crawl(crawlPerimeter, registry);

        assertEquals(2, registry.getPagesCount());
        assertThat(registry.getByUrl(INDEX_URL).get(), is(indexPage));
        assertThat(registry.getByUrl(HOME_URL).get(), is(homePage));
    }
}

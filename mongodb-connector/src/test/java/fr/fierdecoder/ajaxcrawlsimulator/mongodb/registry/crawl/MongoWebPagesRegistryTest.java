package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageFactory;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class MongoWebPagesRegistryTest {
    public static final String AN_URL = "http://mydomain/";
    private static final String ANOTHER_URL = AN_URL + "about";
    private static final String ANOTHER_URL_2 = AN_URL + "contact";
    private static final String ANOTHER_URL_3 = AN_URL + "home";
    public static final String PAGE_TITLE = "title";
    public static final String PAGE_CONTENTS = "contents";
    private WebPageFactory webPageFactory;
    private MongoWebPagesRegistry mongoRegistry, anotherRegistry;
    private HtmlWebPage aHtmlWebPage;
    private RedirectionWebPage aRedirectionWebPage;
    private WebPage anUnreachableWebPage;

    @Before
    public void setUp() throws Exception {
        webPageFactory = new WebPageFactory();
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        mongoRegistry = new MongoWebPagesRegistry("Default", jongo);
        anotherRegistry = new MongoWebPagesRegistry("Another", jongo);
        aHtmlWebPage = webPageFactory.buildHtmlWebPage(AN_URL, 200, PAGE_TITLE, PAGE_CONTENTS, newHashSet());
        aRedirectionWebPage = webPageFactory.buildRedirectionWebPage(ANOTHER_URL, 301, ANOTHER_URL_2, "");
        anUnreachableWebPage = webPageFactory.buildUnreachableWebPage(ANOTHER_URL_3, 404, "Not Found");
    }

    @Test
    public void shouldNotRegisteredUrlNotExist() {
        assertFalse(mongoRegistry.containsUrl("https://www.google.com"));
    }

    @Test
    public void shouldInsertAnHtmlWebPage() {
        mongoRegistry.register(aHtmlWebPage);

        assertTrue(mongoRegistry.containsUrl(AN_URL));
        assertFalse(anotherRegistry.containsUrl(AN_URL));
        Optional<WebPage> result = mongoRegistry.getByUrl(AN_URL);
        assertThat(result.isPresent(), is(true));
        WebPage webPage = result.get();
        assertThat(webPage, instanceOf(HtmlWebPage.class));
        HtmlWebPage htmlResult = webPage.asHtml();
        assertThat(htmlResult, is(aHtmlWebPage));
    }

    @Test
    public void shouldReturnAllPages() {
        mongoRegistry.register(aHtmlWebPage);
        mongoRegistry.register(aRedirectionWebPage);
        mongoRegistry.register(anUnreachableWebPage);

        assertThat(mongoRegistry.getPagesCount(), is(3l));
        assertThat(mongoRegistry.getWebPages(), containsInAnyOrder(aHtmlWebPage, aRedirectionWebPage, anUnreachableWebPage));
        assertThat(anotherRegistry.getPagesCount(), is(0l));
        assertThat(anotherRegistry.getWebPages().size(), is(0));
    }

    @Test
    public void shouldDropAllPages() {
        mongoRegistry.register(aHtmlWebPage);
        anotherRegistry.register(aRedirectionWebPage);
        mongoRegistry.register(anUnreachableWebPage);

        mongoRegistry.drop();

        assertThat(mongoRegistry.getPagesCount(), is(0l));
        assertThat(anotherRegistry.getPagesCount(), is(1l));
    }
}
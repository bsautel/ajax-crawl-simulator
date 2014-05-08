package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.UnreachableWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;

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
    private MongoWebPagesRegistry mongoRegistry;

    @Before
    public void setUp() throws Exception {
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        mongoRegistry = new MongoWebPagesRegistry(jongo);
    }

    @Test
    public void shouldNotRegisteredUrlNotExist() {
        assertFalse(mongoRegistry.containsUrl("https://www.google.com"));
    }

    @Test
    public void shouldInsertAnHtmlWebPage() {
        HtmlWebPage htmlWebPage = new HtmlWebPage(AN_URL, PAGE_TITLE, PAGE_CONTENTS, newHashSet());

        mongoRegistry.register(htmlWebPage);

        assertTrue(mongoRegistry.containsUrl(AN_URL));
        WebPage result = mongoRegistry.getByUrl(AN_URL);
        assertThat(result, instanceOf(HtmlWebPage.class));
        HtmlWebPage htmlResult = result.asHtmlWebPage();
        assertThat(htmlResult, is(htmlWebPage));
    }

    @Test
    public void shouldReturnAllPages() {
        HtmlWebPage htmlWebPage = new HtmlWebPage(AN_URL, PAGE_TITLE, PAGE_CONTENTS, newHashSet());
        RedirectionWebPage redirectionWebPage = new RedirectionWebPage(ANOTHER_URL, ANOTHER_URL_2);
        UnreachableWebPage unreachableWebPage = new UnreachableWebPage(ANOTHER_URL_3, 404);

        mongoRegistry.register(htmlWebPage);
        mongoRegistry.register(redirectionWebPage);
        mongoRegistry.register(unreachableWebPage);

        assertThat(mongoRegistry.getPagesCount(), is(3l));
        assertThat(mongoRegistry.getWebPages(), containsInAnyOrder(htmlWebPage, redirectionWebPage, unreachableWebPage));
    }
}
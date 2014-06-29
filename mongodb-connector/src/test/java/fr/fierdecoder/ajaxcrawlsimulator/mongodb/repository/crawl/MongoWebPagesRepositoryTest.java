package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.*;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository.WebPagePreviewConverter.createWebPagePreview;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class MongoWebPagesRepositoryTest {
    public static final String AN_URL = "http://mydomain/";
    private static final String ANOTHER_URL = AN_URL + "about";
    private static final String ANOTHER_URL_2 = AN_URL + "contact";
    private static final String ANOTHER_URL_3 = AN_URL + "home";
    public static final String PAGE_TITLE = "title";
    public static final String PAGE_CONTENTS = "contents";
    private MongoWebPagesRepository mongoRepository, anotherMongoRepository;
    private HtmlWebPage aHtmlWebPage;
    private RedirectionWebPage aRedirectionWebPage;
    private WebPage anUnreachableWebPage;
    private WebPagePreview aHtmlWebPagePreview, aRedirectionPagePreview, anUnreachablePagePreview;

    @Before
    public void setUp() throws Exception {
        WebPageFactory webPageFactory = new WebPageFactory();
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        mongoRepository = new MongoWebPagesRepository("Default", jongo);
        anotherMongoRepository = new MongoWebPagesRepository("Another", jongo);
        aHtmlWebPage = webPageFactory.buildHtmlWebPage(AN_URL, 200, PAGE_TITLE, PAGE_CONTENTS, newHashSet());
        aRedirectionWebPage = webPageFactory.buildRedirectionWebPage(ANOTHER_URL, 301, ANOTHER_URL_2, "");
        anUnreachableWebPage = webPageFactory.buildUnreachableWebPage(ANOTHER_URL_3, 404, "Not Found");
        aHtmlWebPagePreview = createWebPagePreview(aHtmlWebPage);
        aRedirectionPagePreview = createWebPagePreview(aRedirectionWebPage);
        anUnreachablePagePreview = createWebPagePreview(anUnreachableWebPage);
    }

    @Test
    public void shouldNotRegisteredUrlNotExist() {
        assertFalse(mongoRepository.containsUrl("https://www.google.com"));
    }

    @Test
    public void shouldInsertAnHtmlWebPage() {
        mongoRepository.add(aHtmlWebPage);

        assertTrue(mongoRepository.containsUrl(AN_URL));
        assertFalse(anotherMongoRepository.containsUrl(AN_URL));
        Optional<WebPage> result = mongoRepository.getByUrl(AN_URL);
        assertThat(result.isPresent(), is(true));
        WebPage webPage = result.get();
        assertThat(webPage, instanceOf(HtmlWebPage.class));
        HtmlWebPage htmlResult = webPage.asHtml();
        assertThat(htmlResult, is(aHtmlWebPage));
    }

    @Test
    public void shouldReturnAllPagesPreviews() {
        mongoRepository.add(aHtmlWebPage);
        mongoRepository.add(aRedirectionWebPage);
        mongoRepository.add(anUnreachableWebPage);

        assertThat(mongoRepository.getPagesCount(), is(3l));
        assertThat(mongoRepository.getWebPagesPreviews(),
                containsInAnyOrder(aHtmlWebPagePreview, aRedirectionPagePreview, anUnreachablePagePreview));
        assertThat(anotherMongoRepository.getPagesCount(), is(0l));
        assertThat(anotherMongoRepository.getWebPagesPreviews().size(), is(0));
    }

    @Test
    public void shouldDropAllPages() {
        mongoRepository.add(aHtmlWebPage);
        anotherMongoRepository.add(aRedirectionWebPage);
        mongoRepository.add(anUnreachableWebPage);

        mongoRepository.drop();

        assertThat(mongoRepository.getPagesCount(), is(0l));
        assertThat(anotherMongoRepository.getPagesCount(), is(1l));
    }
}
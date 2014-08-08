package fr.fierdecoder.ajaxcrawlsimulator.crawl.state;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static fr.fierdecoder.ajaxcrawlsimulator.crawl.state.WebPagePreviewConverter.createWebPagePreview;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public abstract class AbstractCrawlStateTest {
    public static final String AN_URL = "http://mydomain/";
    private static final String ANOTHER_URL = AN_URL + "about";
    private static final String ANOTHER_URL_2 = AN_URL + "contact";
    private static final String ANOTHER_URL_3 = AN_URL + "home";
    public static final String PAGE_TITLE = "title";
    public static final String PAGE_CONTENTS = "contents";
    private CrawlState aState, anotherState;
    private WebPage aHtmlWebPage;
    private WebPage aRedirectionWebPage;
    private WebPage anUnreachableWebPage;
    private WebPagePreview aHtmlWebPagePreview, aRedirectionPagePreview, anUnreachablePagePreview;

    @Before
    public void setUp() throws Exception {
        WebPageFactory webPageFactory = new WebPageFactory();
        aState = createCrawlState("Default");
        anotherState = createCrawlState("Another");
        aHtmlWebPage = webPageFactory.buildHtmlWebPage(AN_URL, 200, PAGE_TITLE, PAGE_CONTENTS, newHashSet());
        aRedirectionWebPage = webPageFactory.buildRedirectionWebPage(ANOTHER_URL, 301, ANOTHER_URL_2, "");
        anUnreachableWebPage = webPageFactory.buildUnreachableWebPage(ANOTHER_URL_3, 404, "Not Found");
        aHtmlWebPagePreview = createWebPagePreview(aHtmlWebPage);
        aRedirectionPagePreview = createWebPagePreview(aRedirectionWebPage);
        anUnreachablePagePreview = createWebPagePreview(anUnreachableWebPage);
    }

    protected abstract CrawlState createCrawlState(String aDefault);

    @Test
    public void shouldNotRegisteredUrlNotExist() {
        assertFalse(aState.containsPage("https://www.google.com"));
    }

    @Test
    public void shouldInsertAnHtmlWebPage() {
        aState.addPage(aHtmlWebPage);

        assertTrue(aState.containsPage(AN_URL));
        assertFalse(anotherState.containsPage(AN_URL));
        Optional<WebPage> result = aState.getPageByUrl(AN_URL);
        assertThat(result.isPresent(), is(true));
        WebPage webPage = result.get();
        assertThat(webPage, is(aHtmlWebPage));
    }

    @Test
    public void shouldReturnAllPagesPreviews() {
        aState.addPage(aHtmlWebPage);
        aState.addPage(aRedirectionWebPage);
        aState.addPage(anUnreachableWebPage);

        assertThat(aState.getPagesCount(), is(3l));
        assertThat(aState.getWebPagesPreviews(),
                containsInAnyOrder(aHtmlWebPagePreview, aRedirectionPagePreview, anUnreachablePagePreview));
        assertThat(anotherState.getPagesCount(), is(0l));
        assertThat(anotherState.getWebPagesPreviews().size(), is(0));
    }

    @Test
    public void shouldDropAllPages() {
        aState.addPage(aHtmlWebPage);
        anotherState.addPage(aRedirectionWebPage);
        aState.addPage(anUnreachableWebPage);

        aState.drop();

        assertThat(aState.getPagesCount(), is(0l));
        assertThat(anotherState.getPagesCount(), is(1l));
    }

    @Test
    public void shouldHaveNoUrlToCrawlWhenStarting() {
        assertThat(aState.hasUrlToCrawl(), is(false));
        assertThat(anotherState.hasUrlToCrawl(), is(false));
    }

    @Test
    public void shouldAnUrlToCrawlWhenAnUrlWasAdded() {
        aState.addUrlToCrawl(AN_URL);

        assertThat(aState.hasUrlToCrawl(), is(true));
        assertThat(aState.getUrlToCrawl(), is(AN_URL));
        assertThat(anotherState.hasUrlToCrawl(), is(false));
    }

    @Test
    public void shouldHaveAnUrlToCrawlWhenMultipleUrlsWhereAdded() {
        aState.addUrlsToCrawl(newArrayList(AN_URL, ANOTHER_URL));

        assertThat(aState.hasUrlToCrawl(), is(true));
        assertThat(aState.getUrlToCrawl(), is(AN_URL));
        assertThat(anotherState.hasUrlToCrawl(), is(false));
    }

    @Test
    public void shouldHaveNoLongerUrlToCrawlWhenGettingTheLastOne() {
        aState.addUrlToCrawl(AN_URL);

        aState.getUrlToCrawl();

        assertThat(aState.hasUrlToCrawl(), is(false));
    }

    @Test
    public void shouldBeRunningWhenNotFinished() {
        assertThat(aState.isRunning(), is(true));
        assertThat(anotherState.isRunning(), is(true));
    }

    @Test
    public void shouldTellThatIsNoLongerRunningWhenFinished() {
        aState.maskAsFinished();

        assertThat(aState.isRunning(), is(false));
        assertThat(anotherState.isRunning(), is(true));
    }
}
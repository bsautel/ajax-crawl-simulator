package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.CharStreams;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.NetworkPageReader.ESCAPED_FRAGMENT;
import static java.net.URLEncoder.encode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class NetworkPageReaderTest {
    public static final int PORT = 18089;
    public static final String HTTP_DOMAIN = "http://localhost:" + PORT;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);
    public static final String HOME_PATH = "/home";
    public static final String ANCHOR = "#anchor";
    private static final String ABOUT_PAGE = "/about";
    private static final String A_PAGE = "/a/b";
    private static final String CONTACT_URL = "http://mydomain.com/contact";
    private static final String GOOGLE_URL = "https://www.google.com/";
    private NetworkPageReader networkPageReader;
    private String simpleHtmlDocument, fragmentHtmlDocument, anotherFragmentHtmlDocument, canonicalUrlDocument;

    @Before
    public void setUp() throws Exception {
        simpleHtmlDocument = readHtmlDocument("document.html");
        fragmentHtmlDocument = readHtmlDocument("document_with_fragment.html");
        anotherFragmentHtmlDocument = readHtmlDocument("another_document_with_fragment.html");
        canonicalUrlDocument = readHtmlDocument("document_with_canonical_url.html");
        networkPageReader = new NetworkPageReader(new WebPageFactory());
    }

    private String readHtmlDocument(String fileName) throws IOException {
        return CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream(fileName), "utf-8"));
    }

    @Test
    public void htmlPageWithoutEncoding() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html").withBody(simpleHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        assertHtmlDocument(simpleHtmlDocument);
    }

    private void assertHtmlDocument(String responseBody) {
        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isHtml(), is(true));
        assertThat(result.getHttpStatus(), is(200));
        assertThat(result.getBody(), is(responseBody));
        assertThat(result.getTitle().isPresent(), is(true));
        assertThat(result.getTitle().get(), is("My page"));
        assertThat(result.getLinks(), containsInAnyOrder(HTTP_DOMAIN + ABOUT_PAGE, CONTACT_URL, GOOGLE_URL));
    }

    @Test
    public void htmlPageWithEncoding() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(simpleHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        assertHtmlDocument(simpleHtmlDocument);
    }

    @Test
    public void redirectWithAbsolutePath() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(301).withHeader("Location", GOOGLE_URL);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getTargetUrl().isPresent(), is(true));
        assertThat(result.getTargetUrl().get(), is(GOOGLE_URL));
    }

    @Test
    public void redirectWithRwoRelativesPaths() throws IOException {
        ResponseDefinitionBuilder firstResponse = aResponse().withStatus(302).withHeader("Location", A_PAGE);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(firstResponse));
        ResponseDefinitionBuilder secondResponse = aResponse().withStatus(301).withHeader("Location", ABOUT_PAGE);
        stubFor(get(urlEqualTo(A_PAGE)).willReturn(secondResponse));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getHttpStatus(), is(301));
        assertThat(result.getTargetUrl().isPresent(), is(true));
        assertThat(result.getTargetUrl().get(), is(HTTP_DOMAIN + A_PAGE));
    }

    @Test
    public void notFoundPage() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(404);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isUnreachable(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getHttpStatus(), is(404));
    }

    @Test
    public void textPage() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withBody("foo").withHeader("Content-Type", "text/plain");
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isText(), is(true));
        assertThat(result.getBody(), is("foo"));
        assertThat(result.getHttpStatus(), is(200));
    }

    @Test
    public void binaryPage() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withBody("foo").withHeader("Content-Type", "image/png");
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isBinary(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getHttpStatus(), is(200));
    }

    @Test
    public void duplicatePageWithHash() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(simpleHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH + ANCHOR);

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getHttpStatus(), is(200));
        assertThat(result.getUrl(), is(HTTP_DOMAIN + HOME_PATH + ANCHOR));
        assertThat(result.getTargetUrl().isPresent(), is(true));
        assertThat(result.getTargetUrl().get(), is(HTTP_DOMAIN + HOME_PATH));
    }

    @Test
    public void duplicatePageWithEmptyHash() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(simpleHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH + "#");

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getHttpStatus(), is(200));
        assertThat(result.getUrl(), is(HTTP_DOMAIN + HOME_PATH + "#"));
        assertThat(result.getTargetUrl().isPresent(), is(true));
        assertThat(result.getTargetUrl().get(), is(HTTP_DOMAIN + HOME_PATH));
    }

    @Test
    public void htmlPageWithFragmentSupportAndWithoutHash() throws Exception {
        configureFragmentHtmlDocument();
        ResponseDefinitionBuilder simpleResponse = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(simpleHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH + "?" + ESCAPED_FRAGMENT + "=")).willReturn(simpleResponse));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isHtml(), is(true));
        assertThat(result.getUrl(), is(HTTP_DOMAIN + HOME_PATH));
        assertThat(result.getBody(), is(simpleHtmlDocument));
        assertThat(result.getHttpStatus(), is(200));
    }

    @Test
    public void htmlPageWithFragmentThatIsResolvedAsAPageWithFragments() throws Exception {
        configureFragmentHtmlDocument();
        ResponseDefinitionBuilder anotherFragmentResponse = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(anotherFragmentHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH + "?" + ESCAPED_FRAGMENT + "=")).willReturn(anotherFragmentResponse));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isHtml(), is(true));
        assertThat(result.getUrl(), is(HTTP_DOMAIN + HOME_PATH));
        assertThat(result.getBody(), is(anotherFragmentHtmlDocument));
        assertThat(result.getHttpStatus(), is(200));
    }

    private void configureFragmentHtmlDocument() {
        ResponseDefinitionBuilder fragmentResponse = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(fragmentHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(fragmentResponse));
    }

    @Test
    public void htmlPageWithFragmentSupportAndHash() throws UnsupportedEncodingException {
        String hash = "test;a/b;a=urn:something:a/b!%34";
        configureFragmentHtmlDocument();
        ResponseDefinitionBuilder simpleResponse = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(simpleHtmlDocument);
        stubFor(get(urlEqualTo(HOME_PATH + "?" + ESCAPED_FRAGMENT + "=" + encode(hash, "utf-8"))).willReturn(simpleResponse));

        String url = HTTP_DOMAIN + HOME_PATH + "#!" + hash;
        WebPage result = networkPageReader.readPage(url);

        assertThat(result.isHtml(), is(true));
        assertThat(result.getUrl(), is(url));
        assertThat(result.getBody(), is(simpleHtmlDocument));
        assertThat(result.getHttpStatus(), is(200));
    }

    @Test
    public void shouldConsiderPageWithADifferentCanonicalUrlAsARedirection() {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html").withBody(canonicalUrlDocument);
        stubFor(get(urlEqualTo(ABOUT_PAGE)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + ABOUT_PAGE);

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getHttpStatus(), is(200));
        assertThat(result.getTargetUrl().get(), is(HTTP_DOMAIN + HOME_PATH));
    }

    @Test
    public void shouldConsiderPageWithTheSameCanonicalUrlAsANormalWebPage() {
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html").withBody(canonicalUrlDocument);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isHtml(), is(true));
        assertThat(result.getLinks().size(), is(1));
    }
}
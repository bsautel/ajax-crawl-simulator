package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.CharStreams;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class NetworkPageReaderTest {
    public static final int PORT = 18089;
    public static final String HTTP_DOMAIN = "http://localhost:" + PORT;
    public static final String HOME_PATH = "/home";
    private static final String ABOUT_PAGE = "/about";
    private static final String A_PAGE = "/a/b";
    private static final String CONTACT_URL = "http://mydomain.com/contact";
    private static final String GOOGLE_URL = "https://www.google.com/";
    public static final String ANCHOR = "#anchor";

    private NetworkPageReader networkPageReader;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);

    @Before
    public void setUp() throws Exception {
        networkPageReader = new NetworkPageReader(new DocumentReader(), new WebPageFactory());
    }

    @Test
    public void htmlPageWithoutEncoding() throws IOException {
        String responseBody = readHtmlDocument();
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html").withBody(responseBody);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        assertHtmlDocument(responseBody);
    }

    private void assertHtmlDocument(String responseBody) {
        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isHtml(), is(true));
        assertThat(result.getHttpStatus(), is(200));
        assertThat(result.getBody(), is(responseBody));
        HtmlWebPage htmlWebPage = result.asHtml();
        assertThat(htmlWebPage.getTitle(), is("My page"));
        assertThat(htmlWebPage.getLinks(),
                containsInAnyOrder(HTTP_DOMAIN + ABOUT_PAGE, CONTACT_URL, GOOGLE_URL));
    }

    @Test
    public void htmlPageWithEncoding() throws IOException {
        String responseBody = readHtmlDocument();
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(responseBody);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        assertHtmlDocument(responseBody);
    }

    private String readHtmlDocument() throws IOException {
        return CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("document.html"), "utf-8"));
    }

    @Test
    public void redirectWithAbsolutePath() throws IOException {
        ResponseDefinitionBuilder response = aResponse().withStatus(301).withHeader("Location", GOOGLE_URL);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH);

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getBody(), is(""));
        RedirectionWebPage redirection = result.asRedirection();
        assertThat(redirection.getTargetUrl(), is(GOOGLE_URL));
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
        RedirectionWebPage redirection = result.asRedirection();
        assertThat(redirection.getTargetUrl(), is(HTTP_DOMAIN + A_PAGE));
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
    public void duplicatePage() throws IOException {
        String responseBody = readHtmlDocument();
        ResponseDefinitionBuilder response = aResponse().withStatus(200)
                .withHeader("Content-Type", "text/html; charset=utf-8").withBody(responseBody);
        stubFor(get(urlEqualTo(HOME_PATH)).willReturn(response));

        WebPage result = networkPageReader.readPage(HTTP_DOMAIN + HOME_PATH + ANCHOR);

        assertThat(result.isRedirection(), is(true));
        assertThat(result.getBody(), is(""));
        assertThat(result.getHttpStatus(), is(200));
        assertThat(result.asRedirection().getTargetUrl(), is(HTTP_DOMAIN + HOME_PATH));
    }
}
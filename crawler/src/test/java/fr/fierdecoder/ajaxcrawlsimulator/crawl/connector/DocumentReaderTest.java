package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DocumentReaderTest {
    public static final String ROOT_URL = "http://mydomain.com/";
    private DocumentReader documentReader;
    private Document sampleDocument, documentWithCanonicalUrl;

    @Before
    public void setUp() throws Exception {
        documentReader = new DocumentReader();

        sampleDocument = parseTestDocument("document.html");
        documentWithCanonicalUrl = parseTestDocument("document_with_canonical_url.html");
    }

    private Document parseTestDocument(String fileName) throws IOException {
        InputStream fileStream = getClass().getResourceAsStream(fileName);
        return Jsoup.parse(fileStream, "utf-8", ROOT_URL);
    }

    @Test
    public void readLinks() throws IOException {
        Set<String> links = documentReader.readLinks(sampleDocument);

        assertThat(links, containsInAnyOrder(ROOT_URL + "contact", ROOT_URL + "about", "https://www.google.com/"));
    }

    @Test
    public void pageTitle() {
        String title = documentReader.readTitle(sampleDocument);

        assertEquals("My page", title);
    }

    @Test
    public void shouldNotHaveACanonicalUrl() {
        Optional<String> optionalCanonicalUrl = documentReader.readCanonicalUrl(sampleDocument);

        assertThat(optionalCanonicalUrl.isPresent(), is(false));
    }

    @Test
    public void shouldHaveACanonicalUrl() {
        Optional<String> optionalCanonicalUrl = documentReader.readCanonicalUrl(documentWithCanonicalUrl);

        assertThat(optionalCanonicalUrl.isPresent(), is(true));
        assertThat(optionalCanonicalUrl.get(), is(ROOT_URL + "home"));
    }
}
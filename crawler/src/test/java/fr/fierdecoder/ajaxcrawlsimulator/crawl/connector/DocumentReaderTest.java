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
    private DocumentReader sampleDocument, documentWithCanonicalUrl, documentWithFragment;

    @Before
    public void setUp() throws Exception {
        sampleDocument = new DocumentReader(parseTestDocument("document.html"));
        documentWithCanonicalUrl = new DocumentReader(parseTestDocument("document_with_canonical_url.html"));
        documentWithFragment = new DocumentReader(parseTestDocument("document_with_fragment.html"));
    }

    private Document parseTestDocument(String fileName) throws IOException {
        InputStream fileStream = getClass().getResourceAsStream(fileName);
        return Jsoup.parse(fileStream, "utf-8", ROOT_URL);
    }

    @Test
    public void readLinks() throws IOException {
        Set<String> links = sampleDocument.readLinks();

        assertThat(links, containsInAnyOrder(ROOT_URL + "contact", ROOT_URL + "about", "https://www.google.com/"));
    }

    @Test
    public void pageTitle() {
        String title = sampleDocument.readTitle();

        assertEquals("My page", title);
    }

    @Test
    public void shouldNotHaveACanonicalUrl() {
        Optional<String> optionalCanonicalUrl = sampleDocument.readCanonicalUrl();

        assertThat(optionalCanonicalUrl.isPresent(), is(false));
    }

    @Test
    public void shouldHaveACanonicalUrl() {
        Optional<String> optionalCanonicalUrl = documentWithCanonicalUrl.readCanonicalUrl();

        assertThat(optionalCanonicalUrl.isPresent(), is(true));
        assertThat(optionalCanonicalUrl.get(), is(ROOT_URL + "home"));
    }

    @Test
    public void shouldNotSupportFragment() {
        boolean hasFragment = sampleDocument.supportsFragment();

        assertThat(hasFragment, is(false));
    }

    @Test
    public void shouldSupportFragment() {
        boolean hasFragment = documentWithFragment.supportsFragment();

        assertThat(hasFragment, is(true));
    }
}
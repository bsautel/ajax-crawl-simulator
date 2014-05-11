package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DocumentReaderTest {
    public static final String ROOT_URL = "http://mydomain.com/";
    private DocumentReader documentReader;
    private Document sampleDocument;

    @Before
    public void setUp() throws Exception {
        documentReader = new DocumentReader();

        sampleDocument = parseTestDocument();
    }

    private Document parseTestDocument() throws IOException {
        InputStream fileStream = getClass().getResourceAsStream("document.html");
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
}
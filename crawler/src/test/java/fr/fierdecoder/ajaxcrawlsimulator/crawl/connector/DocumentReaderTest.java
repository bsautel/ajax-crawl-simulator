package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DocumentReaderTest {
    public static final String ROOT_URL = "http://mydomain.com/";
    private DocumentReader documentReader;

    @Before
    public void setUp() throws Exception {
        documentReader = new DocumentReader();
    }

    @Test
    public void readLinks() throws IOException {
        InputStream fileStream = getClass().getResourceAsStream("document.html");
        Document document = Jsoup.parse(fileStream, "utf-8", ROOT_URL);

        Set<String> links = documentReader.readLinks(document);

        assertThat(links, containsInAnyOrder(ROOT_URL + "contact", ROOT_URL + "about", "https://www.google.com"));
    }
}
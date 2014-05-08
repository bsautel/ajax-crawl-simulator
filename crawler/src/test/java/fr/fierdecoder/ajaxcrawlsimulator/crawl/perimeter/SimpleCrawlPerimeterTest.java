package fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleCrawlPerimeterTest {
    public static final String DOMAIN_ROOT = "http://mydomain.com";
    public static final String ENTRY_URL = DOMAIN_ROOT + "/home";
    private SimpleCrawlPerimeter crawlPerimeter;

    @Before
    public void setUp() throws Exception {
        crawlPerimeter = new SimpleCrawlPerimeter(ENTRY_URL, DOMAIN_ROOT);
    }

    @Test
    public void entryUrl() {
        assertEquals(ENTRY_URL, crawlPerimeter.getEntryUrl());
    }

    @Test
    public void shouldDomainRootBeInPerimeter() {
        assertTrue(crawlPerimeter.contains(DOMAIN_ROOT + "/"));
    }

    @Test
    public void shouldDomainPageBeInPerimeter() {
        assertTrue(crawlPerimeter.contains(DOMAIN_ROOT + "/foo/bar"));
    }

    @Test
    public void shouldExternalPageNotBeInPerimeter() {
        assertFalse(crawlPerimeter.contains("https://www.google.com"));
    }
}
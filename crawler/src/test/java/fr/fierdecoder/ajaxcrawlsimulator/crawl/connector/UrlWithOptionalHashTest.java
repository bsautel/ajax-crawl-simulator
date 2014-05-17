package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UrlWithOptionalHashTest {

    public static final String URL = "http://domain.com/path?param=value";
    public static final String HASH = "hash";

    @Test
    public void urlWithNoHash() {
        UrlWithOptionalHash urlWithOptionalHash = UrlWithOptionalHash.create(URL);

        assertThat(urlWithOptionalHash.hasHash(), is(false));
        assertThat(urlWithOptionalHash.hasFragment(), is(false));
        assertThat(urlWithOptionalHash.getUrlWithoutHash(), is(URL));
    }

    @Test
    public void urlWithHash() {
        UrlWithOptionalHash urlWithOptionalHash = UrlWithOptionalHash.create(URL + "#" + HASH);

        assertThat(urlWithOptionalHash.hasHash(), is(true));
        assertThat(urlWithOptionalHash.hasFragment(), is(false));
        assertThat(urlWithOptionalHash.getUrlWithoutHash(), is(URL));
        assertThat(urlWithOptionalHash.getHash(), is(HASH));
    }

    @Test
    public void urlWithFragment() {
        UrlWithOptionalHash urlWithOptionalHash = UrlWithOptionalHash.create(URL + "#!" + HASH);

        assertThat(urlWithOptionalHash.hasHash(), is(true));
        assertThat(urlWithOptionalHash.hasFragment(), is(true));
        assertThat(urlWithOptionalHash.getUrlWithoutHash(), is(URL));
        assertThat(urlWithOptionalHash.getHash(), is("!" + HASH));
        assertThat(urlWithOptionalHash.getFragment(), is(HASH));
    }

    @Test
    public void urlWithEmptyHash() {
        UrlWithOptionalHash urlWithOptionalHash = UrlWithOptionalHash.create(URL + "#");

        assertThat(urlWithOptionalHash.hasHash(), is(true));
        assertThat(urlWithOptionalHash.hasFragment(), is(false));
        assertThat(urlWithOptionalHash.getUrlWithoutHash(), is(URL));
        assertThat(urlWithOptionalHash.getHash(), is(""));
    }
}
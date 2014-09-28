package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPageFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.UrlWithOptionalHash.parse;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class NetworkPageReader implements PageReader {
    public static final String ESCAPED_FRAGMENT = "_escaped_fragment_";
    private final Logger LOGGER = getLogger(NetworkPageReader.class);
    private final WebPageFactory webPageFactory;

    @Inject
    public NetworkPageReader(WebPageFactory webPageFactory) {
        this.webPageFactory = webPageFactory;
    }

    @Override
    public WebPage readPage(String url) {
        try {
            return resolveUrl(url, url);
        } catch (IOException | URISyntaxException e) {
            return webPageFactory.buildUnreachableWebPage(url, 0, "");
        }
    }

    private WebPage resolveUrl(String url, String urlToDisplay)
            throws IOException, URISyntaxException {
        UrlWithOptionalHash urlWithOptionalHash = parse(url);
        if (urlWithOptionalHash.hasFragment()) {
            String fragment = urlWithOptionalHash.getFragment();
            String resolvedUrl = replaceHashByEscapedFragment(urlWithOptionalHash, fragment);
            LOGGER.info("{} has a fragment, it is replaced by a {} query string", url, ESCAPED_FRAGMENT);
            return resolveUrlWithoutFragment(resolvedUrl, url, ResolveFragmentStrategy.DO_NOT_RESOLVE);
        }
        return resolveUrlWithoutFragment(url, urlToDisplay, ResolveFragmentStrategy.RESOLVE);
    }

    private WebPage resolveUrlWithoutFragment(String url, String urlToDisplay,
                                              ResolveFragmentStrategy resolveFragmentStrategy)
            throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        LOGGER.info("Fetching {}", url);
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request, context)) {
            return readResponse(urlToDisplay, resolveFragmentStrategy, context, response);
        }
    }

    private WebPage readResponse(String urlToDisplay, ResolveFragmentStrategy resolveFragmentStrategy,
                                 HttpClientContext context, CloseableHttpResponse response)
            throws IOException, URISyntaxException {
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        InputStream body = entity.getContent();
        ContentType contentType = ContentType.getOrDefault(entity);
        String bodyString = stringify(body, contentType);
        if (isRedirection(context)) {
            LOGGER.debug("{} is a redirection", urlToDisplay);
            URI location = context.getRedirectLocations().get(0);
            // TODO find a way to get the right first redirection
            return webPageFactory.buildRedirectionWebPage(urlToDisplay, 301, "", location.toString());
        }
        if (isHttpError(status)) {
            LOGGER.debug("{} is a redirection", urlToDisplay);
            return webPageFactory.buildUnreachableWebPage(urlToDisplay, status, bodyString);
        }
        if (isHtmlPage(contentType)) {
            LOGGER.debug("{} is a HTML page", urlToDisplay);
            return processHtmlWebPage(urlToDisplay, status, bodyString, resolveFragmentStrategy);
        }
        if (isTextPage(contentType)) {
            LOGGER.debug("{} is a text page", urlToDisplay);
            return webPageFactory.buildTextWebPage(urlToDisplay, status, bodyString);
        }
        LOGGER.debug("{} is a binary file", urlToDisplay);
        return webPageFactory.buildBinaryWebPage(urlToDisplay, status);
    }

    private String stringify(InputStream body, ContentType contentType) {
        try {
            String charsetName = ofNullable(contentType.getCharset()).map(Charset::name).orElse("utf-8");
            return CharStreams.toString(new InputStreamReader(body, charsetName));
        } catch (IOException e) {
            return "No text contents";
        }
    }

    private boolean isHttpError(int status) {
        return status < 200 || status >= 300;
    }

    private boolean isRedirection(HttpClientContext context) {
        List<URI> redirectLocations = context.getRedirectLocations();
        return redirectLocations != null && !redirectLocations.isEmpty();
    }

    private boolean isHtmlPage(ContentType contentType) {
        return contentType.getMimeType().equals("text/html");
    }

    private boolean isTextPage(ContentType contentType) {
        return contentType.getMimeType().startsWith("text/");
    }

    private WebPage processHtmlWebPage(String url, int status, String body, ResolveFragmentStrategy resolveFragmentStrategy)
            throws IOException, URISyntaxException {
        UrlWithOptionalHash urlWithOptionalHash = parse(url);
        Document document = Jsoup.parse(body, url);
        DocumentReader documentReader = new DocumentReader(document);
        Optional<String> optionalCanonicalUrl = documentReader.readCanonicalUrl();
        if (optionalCanonicalUrl.isPresent()) {
            String canonicalUrl = optionalCanonicalUrl.get();
            if (!canonicalUrl.equals(url)) {
                return webPageFactory.buildRedirectionWebPage(url, status, body, canonicalUrl);
            }
        }
        if (urlWithOptionalHash.hasHash() && !urlWithOptionalHash.hasFragment()) {
            String canonicalUrl = urlWithOptionalHash.getUrlWithoutHash();
            return webPageFactory.buildRedirectionWebPage(url, 200, "", canonicalUrl);
        }
        if (resolveFragmentStrategy.canResolveFragment() && documentReader.supportsFragment()) {
            String resolvedUrl = replaceHashByEscapedFragment(urlWithOptionalHash, "");
            return resolveUrlWithoutFragment(resolvedUrl, url, ResolveFragmentStrategy.DO_NOT_RESOLVE);
        }
        Set<String> links = documentReader.readLinks();
        String title = documentReader.readTitle();
        return webPageFactory.buildHtmlWebPage(url, status, title, body, links);
    }

    private String replaceHashByEscapedFragment(UrlWithOptionalHash url, String fragment) throws URISyntaxException {
        URIBuilder urlBuilder = new URIBuilder(url.getUrlWithoutHash());
        urlBuilder.addParameter(ESCAPED_FRAGMENT, fragment);
        return urlBuilder.toString();
    }

    private static enum ResolveFragmentStrategy {
        RESOLVE(true), DO_NOT_RESOLVE(false);

        private boolean canResolveFragment;

        private ResolveFragmentStrategy(boolean canResolveFragment) {
            this.canResolveFragment = canResolveFragment;
        }

        public boolean canResolveFragment() {
            return canResolveFragment;
        }
    }
}

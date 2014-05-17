package fr.fierdecoder.ajaxcrawlsimulator.crawl.connector;

import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPageFactory;
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
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import static fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.UrlWithOptionalHash.create;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class NetworkPageReader implements PageReader {
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

    public static final String ESCAPED_FRAGMENT = "_escaped_fragment_";
    private final Logger LOGGER = getLogger(NetworkPageReader.class);
    private final DocumentReader documentReader;
    private final WebPageFactory webPageFactory;

    @Inject
    public NetworkPageReader(DocumentReader documentReader, WebPageFactory webPageFactory) {
        this.documentReader = documentReader;
        this.webPageFactory = webPageFactory;
    }

    @Override
    public WebPage readPage(String url) {
        try {
            return resolveUrl(url, url, ResolveFragmentStrategy.RESOLVE);
        } catch (IOException | URISyntaxException e) {
            return webPageFactory.buildUnreachableWebPage(url, 0, "");
        }
    }

    private WebPage resolveUrl(String url, String urlToDisplay, ResolveFragmentStrategy resolveFragmentStrategy)
            throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        LOGGER.info("Fetching {}", url);
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request, context)) {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            InputStream body = entity.getContent();
            ContentType contentType = ContentType.getOrDefault(entity);
            String bodyString = stringify(body, contentType);
            if (isRedirection(context)) {
                LOGGER.debug("{} is a redirection", url);
                URI location = context.getRedirectLocations().get(0);
                // TODO find a way to get the right first redirection
                return webPageFactory.buildRedirectionWebPage(urlToDisplay, 301, "", location.toString());
            }
            if (isHttpError(status)) {
                LOGGER.debug("{} is a redirection", url);
                return webPageFactory.buildUnreachableWebPage(urlToDisplay, status, bodyString);
            }
            if (isHtmlPage(contentType)) {
                LOGGER.debug("{} is a HTML page", url);
                return processHtmlWebPage(urlToDisplay, status, bodyString, resolveFragmentStrategy);
            }
            if (isTextPage(contentType)) {
                LOGGER.debug("{} is a text page", url);
                return webPageFactory.buildTextWebPage(urlToDisplay, status, bodyString);
            }
            LOGGER.debug("{} is a binary file", url);
            return webPageFactory.buildBinaryWebPage(urlToDisplay, status);
        }
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
        UrlWithOptionalHash urlWithOptionalHash = create(url);
        Document document = Jsoup.parse(body, url);
        boolean supportsFragment = supportsFragment(document);
        if (resolveFragmentStrategy.canResolveFragment() && supportsFragment) {
            if (urlWithOptionalHash.hasFragment()) {
                String fragment = urlWithOptionalHash.getFragment();
                String resolvedUrl = replaceHashByEscapedFragment(url, fragment);
                return resolveUrl(resolvedUrl, url, ResolveFragmentStrategy.DO_NOT_RESOLVE);
            } else {
                String resolvedUrl = replaceHashByEscapedFragment(url, "");
                return resolveUrl(resolvedUrl, url, ResolveFragmentStrategy.DO_NOT_RESOLVE);
            }
        } else if (urlWithOptionalHash.hasHash() && !urlWithOptionalHash.hasFragment()) {
            String canonicalUrl = urlWithOptionalHash.getUrlWithoutHash();
            return webPageFactory.buildRedirectionWebPage(url, 200, "", canonicalUrl);
        }
        Set<String> links = documentReader.readLinks(document);
        String title = documentReader.readTitle(document);
        return webPageFactory.buildHtmlWebPage(url, status, title, body, links);
    }

    private String replaceHashByEscapedFragment(String url, String fragment) throws URISyntaxException {
        URIBuilder urlBuilder = new URIBuilder(url);
        urlBuilder.addParameter(ESCAPED_FRAGMENT, fragment);
        urlBuilder.setFragment(null);
        return urlBuilder.toString();
    }

    private boolean supportsFragment(Document document) {
        Elements fragmentMeta = document.select("meta[name=fragment]");
        return fragmentMeta.size() == 1;
    }
}

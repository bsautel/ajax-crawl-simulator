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
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Singleton
public class NetworkPageReader implements PageReader {
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
            return resolveUrl(url);
        } catch (IOException e) {
            return webPageFactory.buildUnreachableWebPage(url, 0, "");
        }
    }

    private WebPage resolveUrl(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request, context);
        try {
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            InputStream body = entity.getContent();
            ContentType contentType = ContentType.getOrDefault(entity);
            String bodyString = stringify(body, contentType);
            if (isRedirection(context)) {
                URI location = context.getRedirectLocations().get(0);
                // TODO find a way to get the right first redirection
                return webPageFactory.buildRedirectionWebPage(url, 301, "", location.toString());
            }
            if (isHttpError(status)) {
                return webPageFactory.buildUnreachableWebPage(url, status, bodyString);
            }
            if (isHtmlPage(contentType)) {
                if (url.contains("#")) {
                    String canonicalUrl = url.split("#")[0];
                    return webPageFactory.buildRedirectionWebPage(url, 200, "", canonicalUrl);
                }
                return processHtmlWebPage(url, status, bodyString);
            } else if (isTextPage(contentType)) {
                return webPageFactory.buildTextWebPage(url, status, bodyString);
            }
            return webPageFactory.buildBinaryWebPage(url, status);
        } finally {
            response.close();
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

    private WebPage processHtmlWebPage(String url, int status, String body) {
        Document document = Jsoup.parse(body, url);
        /*Elements fragmentMeta = document.select("meta[name=fragment]");
        if (fragmentMeta.size() == 1) {
            String fragmentMetaValue = fragmentMeta.get(0).attr("content");
            return readPage(url.replace("#", "?_escaped_fragment_="));
        }*/
        Set<String> links = documentReader.readLinks(document);
        String title = documentReader.readTitle(document);
        return webPageFactory.buildHtmlWebPage(url, status, title, body, links);
    }
}

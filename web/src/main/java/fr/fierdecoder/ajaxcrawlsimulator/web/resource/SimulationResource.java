package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPagePreview;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.CrawlSimulator;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.web.value.JsonPage;
import fr.fierdecoder.ajaxcrawlsimulator.web.value.JsonPageConverter;
import fr.fierdecoder.ajaxcrawlsimulator.web.value.JsonPagePreview;
import net.codestory.http.annotations.Delete;
import net.codestory.http.annotations.Get;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

public class SimulationResource {
    private final CrawlSimulator crawlSimulator;
    private final JsonPageConverter jsonPageConverter;

    @Inject
    public SimulationResource(CrawlSimulator crawlSimulator, JsonPageConverter jsonPageConverter) {
        this.crawlSimulator = crawlSimulator;
        this.jsonPageConverter = jsonPageConverter;
    }

    @Get("/simulations/:name")
    public Optional<SimulationDescriptor> getSimulation(String name) {
        return crawlSimulator.getSimulationDescriptorByName(name);
    }

    @Delete("/simulations/:name")
    public void deleteSimulation(String name) {
        crawlSimulator.deleteByName(name);
    }

    @Get("/simulations/:name/pages")
    public Optional<Collection<JsonPagePreview>> getSimulationPages(String name) {
        Optional<CrawlState> optionalCrawlState = crawlSimulator.getSimulationStateByName(name);
        if (optionalCrawlState.isPresent()) {
            Collection<WebPagePreview> webPages = optionalCrawlState.get().getWebPagesPreviews();
            Set<JsonPagePreview> jsonPagePreviews = webPages.stream()
                    .map(jsonPageConverter::createJsonPagePreview)
                    .collect(toSet());
            return of(jsonPagePreviews);
        }
        return empty();
    }

    @Get("/simulations/:name/pages/:url")
    public Optional<JsonPage> getSimulationPage(String name, String url) throws UnsupportedEncodingException {
        String decodedUrl = URLDecoder.decode(url, "UTF-8");
        Optional<CrawlState> optionalCrawlState = crawlSimulator.getSimulationStateByName(name);
        Optional<WebPage> optionalPage = optionalCrawlState.flatMap(repository -> repository.getByUrl(decodedUrl));
        return optionalPage.map(jsonPageConverter::createJsonPage);
    }
}

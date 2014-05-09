package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.CrawlSimulator;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.web.value.JsonPage;
import fr.fierdecoder.ajaxcrawlsimulator.web.value.JsonPageConverter;
import fr.fierdecoder.ajaxcrawlsimulator.web.value.JsonPagePreview;
import net.codestory.http.annotations.Delete;
import net.codestory.http.annotations.Get;

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
        Optional<WebPagesRegistry> webPagesRegistry = crawlSimulator.getSimulationWebPagesRegistryByName(name);
        if (webPagesRegistry.isPresent()) {
            Collection<WebPage> webPages = webPagesRegistry.get().getWebPages();
            Set<JsonPagePreview> jsonPagePreviews = webPages.stream()
                    .map(jsonPageConverter::createJsonPagePreview)
                    .collect(toSet());
            return of(jsonPagePreviews);
        }
        return empty();
    }

    @Get("/simulations/:name/pages/:url")
    public Optional<JsonPage> getSimulationPage(String name, String url) {
        System.out.println(url);
        Optional<WebPagesRegistry> optionalWebPagesRegistry = crawlSimulator.getSimulationWebPagesRegistryByName(name);
        // TODO make getByUrl return an optional in order to avoid requiring call contains
        Optional<WebPage> optionalPage = optionalWebPagesRegistry.map(registry -> registry.getByUrl(url));
        return optionalPage.map(jsonPageConverter::createJsonPage);
    }
}

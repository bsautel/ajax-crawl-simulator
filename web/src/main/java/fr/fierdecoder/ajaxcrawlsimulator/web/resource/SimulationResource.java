package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.CrawlSimulator;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.Simulation;
import net.codestory.http.annotations.Get;

import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class SimulationResource {
    private final CrawlSimulator crawlSimulator;

    @Inject
    public SimulationResource(CrawlSimulator crawlSimulator) {
        this.crawlSimulator = crawlSimulator;
    }

    @Get("/simulations/:name")
    public Optional<Simulation> getSimulation(String name) {
        return crawlSimulator.getSimulationByName(name);
    }

    @Get("/simulations/:name/result")
    public Collection<WebPage> getSimulationResult(String name) {
        return crawlSimulator.getSimulationStatusByName(name)
                .map(WebPagesRegistry::getWebPages)
                .orElse(newArrayList());
    }
}

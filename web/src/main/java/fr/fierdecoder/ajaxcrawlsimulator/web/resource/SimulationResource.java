package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.CrawlSimulator;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
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
    public Optional<SimulationDescriptor> getSimulation(String name) {
        return crawlSimulator.getSimulationDescriptorByName(name);
    }

    @Get("/simulations/:name/result")
    public Collection<WebPage> getSimulationResult(String name) {
        return crawlSimulator.getSimulationWebPagesRegistryByName(name)
                .map(WebPagesRegistry::getWebPages)
                .orElse(newArrayList());
    }
}

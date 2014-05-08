package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.SimpleCrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.SimulationRegistry;

import java.util.Optional;
import java.util.Set;

@Singleton
public class CrawlSimulator {
    private final Crawler crawler;
    private final SimulationRegistry simulationRegistry;
    private final WebPagesRegistryFactory webPagesRegistryFactory;


    @Inject
    public CrawlSimulator(Crawler crawler, SimulationRegistry simulationRegistry,
                          WebPagesRegistryFactory webPagesRegistryFactory) {
        this.crawler = crawler;
        this.simulationRegistry = simulationRegistry;
        this.webPagesRegistryFactory = webPagesRegistryFactory;
    }

    public void start(SimulationDescriptor simulationDescriptor) {
        Simulation result = launchCrawl(simulationDescriptor);
        simulationRegistry.register(result);
    }

    private Simulation launchCrawl(SimulationDescriptor simulationDescriptor) {
        CrawlPerimeter perimeter = new SimpleCrawlPerimeter(simulationDescriptor.getEntryUrl(), simulationDescriptor.getUrlPrefix());
        WebPagesRegistry webPagesRegistry = webPagesRegistryFactory.create(simulationDescriptor.getName());
        crawler.crawl(perimeter, webPagesRegistry);
        return new Simulation(simulationDescriptor, webPagesRegistry);
    }

    public Optional<SimulationDescriptor> getSimulationDescriptorByName(String name) {
        return getSimulationByName(name)
                .map(Simulation::getDescriptor);
    }

    private Optional<Simulation> getSimulationByName(String name) {
        return simulationRegistry.get(name);
    }

    public Optional<WebPagesRegistry> getSimulationWebPagesRegistryByName(String name) {
        return getSimulationByName(name)
                .map(Simulation::getWebPagesRegistry);
    }

    public Set<SimulationDescriptor> getSimulations() {
        return simulationRegistry.getDescriptors();
    }
}

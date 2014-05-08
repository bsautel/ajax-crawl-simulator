package fr.fierdecoder.ajaxcrawlsimulator.web.simulation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.MemoryWebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.registry.WebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.SimpleCrawlPerimeter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

@Singleton
public class CrawlSimulator {
    private final Crawler crawler;
    private final Map<String, SimulationResult> simulations = newHashMap();

    @Inject
    public CrawlSimulator(Crawler crawler) {
        this.crawler = crawler;
    }

    public synchronized void start(Simulation simulation) {
        SimulationResult result = launchCrawl(simulation);
        simulations.put(result.getName(), result);
    }

    private SimulationResult launchCrawl(Simulation simulation) {
        CrawlPerimeter perimeter = new SimpleCrawlPerimeter(simulation.getEntryUrl(), simulation.getUrlPrefix());
        MemoryWebPagesRegistry webPagesRegistry = new MemoryWebPagesRegistry();
        crawler.crawl(perimeter, webPagesRegistry);
        return new SimulationResult(simulation, webPagesRegistry);
    }

    public synchronized Optional<Simulation> getSimulationByName(String name) {
        return getSimulationResultByName(name)
                .map(SimulationResult::getSimulation);
    }

    private Optional<SimulationResult> getSimulationResultByName(String name) {
        if (simulations.containsKey(name)) {
            return of(simulations.get(name));
        }
        return empty();
    }

    public synchronized  Optional<WebPagesRegistry> getSimulationStatusByName(String name) {
        return getSimulationResultByName(name)
                .map(SimulationResult::getWebPagesRegistry);
    }

    public synchronized Set<Simulation> getSimulations() {
        return simulations.values().stream()
                .map(SimulationResult::getSimulation)
                .collect(toSet());
    }
}

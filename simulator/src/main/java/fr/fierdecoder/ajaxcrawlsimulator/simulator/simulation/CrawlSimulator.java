package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.SimpleCrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepository;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.repository.WebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;

import java.util.Optional;
import java.util.Set;

@Singleton
public class CrawlSimulator {
    private final Crawler crawler;
    private final SimulationRepository simulationRepository;
    private final WebPagesRepositoryFactory webPagesRepositoryFactory;


    @Inject
    public CrawlSimulator(Crawler crawler, SimulationRepository simulationRepository,
                          WebPagesRepositoryFactory webPagesRepositoryFactory) {
        this.crawler = crawler;
        this.simulationRepository = simulationRepository;
        this.webPagesRepositoryFactory = webPagesRepositoryFactory;
    }

    public void start(SimulationDescriptor simulationDescriptor) {
        Simulation result = launchCrawl(simulationDescriptor);
        simulationRepository.add(result);
    }

    private Simulation launchCrawl(SimulationDescriptor simulationDescriptor) {
        CrawlPerimeter perimeter = new SimpleCrawlPerimeter(simulationDescriptor.entryUrl(), simulationDescriptor.urlPrefix());
        WebPagesRepository webPagesRepository = webPagesRepositoryFactory.create(simulationDescriptor.name());
        crawler.crawl(perimeter, webPagesRepository);
        return Simulation.create(simulationDescriptor, webPagesRepository);
    }

    public Optional<SimulationDescriptor> getSimulationDescriptorByName(String name) {
        return getSimulationByName(name)
                .map(Simulation::getDescriptor);
    }

    private Optional<Simulation> getSimulationByName(String name) {
        return simulationRepository.get(name);
    }

    public Optional<WebPagesRepository> getSimulationWebPagesRepositoryByName(String name) {
        return getSimulationByName(name)
                .map(Simulation::getWebPagesRepository);
    }

    public Set<SimulationDescriptor> getSimulations() {
        return simulationRepository.getDescriptors();
    }

    public void deleteByName(String name) {
        simulationRepository.deleteByName(name);
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.SimpleCrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository.WebPagesRepository;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.repository.WebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;

import java.util.Optional;
import java.util.Set;

@Singleton
public class CrawlSimulator {
    private final Crawler crawler;
    private final SimulationRepository simulationRepository;
    private final WebPagesRepositoryFactory webPagesRepositoryFactory;
    private final CrawlStateFactory crawlStateFactory;


    @Inject
    public CrawlSimulator(Crawler crawler, SimulationRepository simulationRepository,
                          WebPagesRepositoryFactory webPagesRepositoryFactory,
                          CrawlStateFactory crawlStateFactory) {
        this.crawler = crawler;
        this.simulationRepository = simulationRepository;
        this.webPagesRepositoryFactory = webPagesRepositoryFactory;
        this.crawlStateFactory = crawlStateFactory;
    }

    public void start(SimulationDescriptor simulationDescriptor) {
        Simulation result = launchCrawl(simulationDescriptor);
        simulationRepository.add(result);
    }

    private Simulation launchCrawl(SimulationDescriptor simulationDescriptor) {
        CrawlPerimeter perimeter = new SimpleCrawlPerimeter(simulationDescriptor.getEntryUrl(), simulationDescriptor.getUrlPrefix());
        WebPagesRepository webPagesRepository = webPagesRepositoryFactory.create(simulationDescriptor.getName());
        CrawlState state = crawlStateFactory.create(simulationDescriptor.getName());
        crawler.crawl(perimeter, webPagesRepository, state);
        return new Simulation(simulationDescriptor, webPagesRepository, state);
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

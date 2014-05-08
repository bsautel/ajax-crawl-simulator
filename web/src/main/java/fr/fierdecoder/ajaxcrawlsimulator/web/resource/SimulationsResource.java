package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.CrawlSimulator;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Post;

import java.util.Collection;

public class SimulationsResource {
    private final CrawlSimulator crawlSimulator;

    @Inject
    public SimulationsResource(CrawlSimulator crawlSimulator) {
        this.crawlSimulator = crawlSimulator;
    }

    @Get("/simulations")
    public Collection<Simulation> getSimulations() {
        return crawlSimulator.getSimulations();
    }

    @Post("/simulations")
    public Simulation createSimulation(Simulation simulation) {
        // TODO should not accept to create a simulation that already exists
        crawlSimulator.start(simulation);
        return simulation;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.CrawlSimulator;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
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
    public Collection<SimulationDescriptor> getSimulations() {
        return crawlSimulator.getSimulations();
    }

    @Post("/simulations")
    public SimulationDescriptor createSimulation(SimulationDescriptor simulationDescriptor) {
        // TODO should not accept to parse a simulation that already exists
        crawlSimulator.start(simulationDescriptor);
        return simulationDescriptor;
    }
}

package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.CrawlSimulator;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Post;
import net.codestory.http.payload.Payload;

import java.util.Collection;

import static net.codestory.http.constants.HttpStatus.CONFLICT;
import static net.codestory.http.constants.HttpStatus.CREATED;

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
    public Payload createSimulation(SimulationDescriptor simulationDescriptor) {
        if (simulationAlreadyExists(simulationDescriptor)) {
            return new Payload(CONFLICT);
        }
        crawlSimulator.start(simulationDescriptor);
        return new Payload(simulationDescriptor).withCode(CREATED);
    }

    private boolean simulationAlreadyExists(SimulationDescriptor simulationDescriptor) {
        return crawlSimulator.getSimulationDescriptorByName(simulationDescriptor.getName()).isPresent();
    }
}

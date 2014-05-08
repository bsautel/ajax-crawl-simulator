package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.SimulationsRegistry;
import net.codestory.http.annotations.Get;

import java.util.Optional;

public class SimulationResource {
    private final SimulationsRegistry simulationsRegistry;

    @Inject
    public SimulationResource(SimulationsRegistry simulationsRegistry) {
        this.simulationsRegistry = simulationsRegistry;
    }

    @Get("/simulations/:name")
    public Optional<Simulation> getSimulation(String name) {
        return simulationsRegistry.getByName(name);
    }
}

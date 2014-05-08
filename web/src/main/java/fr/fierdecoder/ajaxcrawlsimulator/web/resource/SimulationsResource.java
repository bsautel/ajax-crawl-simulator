package fr.fierdecoder.ajaxcrawlsimulator.web.resource;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.web.simulation.SimulationsRegistry;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Post;

import java.util.Collection;

public class SimulationsResource {
    private final SimulationsRegistry simulationsRegistry;

    @Inject
    public SimulationsResource(SimulationsRegistry simulationsRegistry) {
        this.simulationsRegistry = simulationsRegistry;
    }

    @Get("/simulations")
    public Collection<Simulation> listSimulations() {
        return simulationsRegistry.getSimulations();
    }

    @Post("/simulations")
    public void createSimulation(Simulation simulation) {
        simulationsRegistry.register(simulation);
    }
}

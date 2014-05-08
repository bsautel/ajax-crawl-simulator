package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry;

import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

public class MemorySimulationsRegistry implements SimulationRegistry {
    private final Map<String, Simulation> simulations = new ConcurrentHashMap<>();

    @Override
    public void register(Simulation simulation) {
        simulations.put(simulation.getName(), simulation);
    }

    @Override
    public Optional<Simulation> get(String name) {
        if (simulations.containsKey(name)) {
            return of(simulations.get(name));
        }
        return empty();
    }

    @Override
    public Set<SimulationDescriptor> getDescriptors() {
        return simulations.values().stream()
                .map(Simulation::getDescriptor)
                .collect(toSet());
    }
}

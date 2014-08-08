package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository;

import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

public class MemorySimulationsRepository implements SimulationRepository {
    private final Map<String, Simulation> simulations = new ConcurrentHashMap<>();

    @Override
    public void add(Simulation simulation) {
        simulations.put(simulation.getName(), simulation);
    }

    @Override
    public Optional<Simulation> get(String name) {
        return ofNullable(simulations.get(name));
    }

    @Override
    public Set<SimulationDescriptor> getDescriptors() {
        return simulations.values().stream()
                .map(Simulation::getDescriptor)
                .collect(toSet());
    }

    @Override
    public void deleteByName(String name) {
        Optional<Simulation> simulation = get(name);
        if (simulation.isPresent()) {
            simulation.get().getState().drop();
        }
        simulations.remove(name);
    }
}

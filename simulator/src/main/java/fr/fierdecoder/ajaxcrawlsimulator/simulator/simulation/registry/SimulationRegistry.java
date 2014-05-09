package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry;

import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;

import java.util.Optional;
import java.util.Set;

public interface SimulationRegistry {
    void register(Simulation simulation);

    Optional<Simulation> get(String name);

    Set<SimulationDescriptor> getDescriptors();

    void deleteByName(String name);
}

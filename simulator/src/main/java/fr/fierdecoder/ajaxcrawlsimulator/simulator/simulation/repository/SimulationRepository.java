package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository;

import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;

import java.util.Optional;
import java.util.Set;

public interface SimulationRepository {
    void add(Simulation simulation);

    Optional<Simulation> get(String name);

    Set<SimulationDescriptor> getDescriptors();

    void deleteByName(String name);
}

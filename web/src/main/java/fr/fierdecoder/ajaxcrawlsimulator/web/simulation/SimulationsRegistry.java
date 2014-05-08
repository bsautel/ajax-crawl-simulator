package fr.fierdecoder.ajaxcrawlsimulator.web.simulation;

import com.google.inject.Singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Singleton
public class SimulationsRegistry {
    private HashMap<String, Simulation> simulations = newHashMap();

    public void register(Simulation simulation) {
        simulations.put(simulation.getName(), simulation);
    }

    public Optional<Simulation> getByName(String name) {
        if (simulations.containsKey(name)) {
            return of(simulations.get(name));
        }
        return empty();
    }

    public Collection<Simulation> getSimulations() {
        return simulations.values();
    }
}

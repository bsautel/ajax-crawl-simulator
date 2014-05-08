package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.simulator;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoDbWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.SimulationRegistry;
import org.jongo.MongoCollection;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class MongoDbSimulationRegistry implements SimulationRegistry {
    private final MongoCollection collection;
    private final MongoDbWebPagesRegistryFactory webPagesRegistryFactory;

    @Inject
    public MongoDbSimulationRegistry(JongoConnectionFactory jongoConnectionFactory,
                                     MongoDbWebPagesRegistryFactory webPagesRegistryFactory) {
        collection = jongoConnectionFactory.create().getCollection("simulations");
        this.webPagesRegistryFactory = webPagesRegistryFactory;
    }

    @Override
    public void register(Simulation simulation) {
        collection.insert(simulation.getDescriptor());
    }

    @Override
    public Optional<Simulation> get(String name) {
        SimulationDescriptor simulation = collection.findOne("{'name': '#'}", name).as(SimulationDescriptor.class);
        return ofNullable(simulation).map(this::createSimulation);
    }

    private Simulation createSimulation(SimulationDescriptor descriptor) {
        return new Simulation(descriptor, webPagesRegistryFactory.create(descriptor.getName()));
    }

    @Override
    public Set<SimulationDescriptor> getDescriptors() {
        Iterable<SimulationDescriptor> descriptors = collection.find().as(SimulationDescriptor.class);
        return Sets.newHashSet(descriptors);
    }
}

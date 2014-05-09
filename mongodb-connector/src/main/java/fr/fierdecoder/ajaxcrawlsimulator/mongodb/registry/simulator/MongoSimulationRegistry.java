package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.simulator;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoDbWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoWebPagesRegistry;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.registry.SimulationRegistry;
import org.jongo.MongoCollection;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class MongoSimulationRegistry implements SimulationRegistry {
    public static final String NAME_FILTER = "{'name': '#'}";
    private final MongoCollection collection;
    private final MongoDbWebPagesRegistryFactory webPagesRegistryFactory;

    @Inject
    public MongoSimulationRegistry(JongoConnectionFactory jongoConnectionFactory,
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
        SimulationDescriptor simulation = collection.findOne(NAME_FILTER, name).as(SimulationDescriptor.class);
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

    @Override
    public void deleteByName(String name) {
        Optional<Simulation> simulation = get(name);
        if (simulation.isPresent()) {
            collection.remove(NAME_FILTER, name);
            MongoWebPagesRegistry mongoWebPagesRegistry = (MongoWebPagesRegistry) simulation.get().getWebPagesRegistry();
            mongoWebPagesRegistry.drop();
        }
    }
}

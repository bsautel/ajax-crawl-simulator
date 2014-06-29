package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbCrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbCrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;
import org.jongo.MongoCollection;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class MongoSimulationRepository implements SimulationRepository {
    public static final String NAME_FILTER = "{'name': '#'}";
    private final MongoCollection collection;
    private final MongoDbCrawlStateFactory crawlStateFactory;

    @Inject
    public MongoSimulationRepository(JongoConnectionFactory jongoConnectionFactory,
                                     MongoDbCrawlStateFactory crawlStateFactory) {
        collection = jongoConnectionFactory.create().getCollection("simulations");
        this.crawlStateFactory = crawlStateFactory;
    }

    @Override
    public void add(Simulation simulation) {
        collection.insert(simulation.getDescriptor());
    }

    @Override
    public Optional<Simulation> get(String name) {
        SimulationDescriptor simulation = collection.findOne(NAME_FILTER, name).as(SimulationDescriptor.class);
        return ofNullable(simulation).map(this::createSimulation);
    }

    private Simulation createSimulation(SimulationDescriptor descriptor) {
//        return new Simulation(descriptor, crawlStateFactory.create(descriptor.getName()), null);
        throw new UnsupportedOperationException("Not yet implemented");
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
            MongoDbCrawlState mongoDbCrawlState = (MongoDbCrawlState) simulation.get().getState();
            mongoDbCrawlState.drop();
        }
    }
}

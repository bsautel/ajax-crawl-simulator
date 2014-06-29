package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbWebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoWebPagesRepository;
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
    private final MongoDbWebPagesRepositoryFactory webPagesRepositoryFactory;

    @Inject
    public MongoSimulationRepository(JongoConnectionFactory jongoConnectionFactory,
                                     MongoDbWebPagesRepositoryFactory webPagesRepositoryFactory) {
        collection = jongoConnectionFactory.create().getCollection("simulations");
        this.webPagesRepositoryFactory = webPagesRepositoryFactory;
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
//        return new Simulation(descriptor, webPagesRepositoryFactory.create(descriptor.getName()), null);
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
            MongoWebPagesRepository mongoWebPagesRepository = (MongoWebPagesRepository) simulation.get().getWebPagesRepository();
            mongoWebPagesRepository.drop();
        }
    }
}

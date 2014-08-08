package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;
import org.jongo.MongoCollection;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Optional.ofNullable;

public class MongoDbSimulationRepository implements SimulationRepository {
    public static final String NAME_FILTER = "{name: #}";
    private final MongoCollection collection;
    private final CrawlStateFactory crawlStateFactory;

    @Inject
    public MongoDbSimulationRepository(JongoConnectionFactory jongoConnectionFactory,
                                       CrawlStateFactory crawlStateFactory) {
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
        return Simulation.create(descriptor, crawlStateFactory.create(descriptor.name()));
    }

    @Override
    public Set<SimulationDescriptor> getDescriptors() {
        Iterable<SimulationDescriptor> descriptors = collection.find().as(SimulationDescriptor.class);
        return newHashSet(descriptors);
    }

    @Override
    public void deleteByName(String name) {
        Optional<Simulation> simulation = get(name);
        if (simulation.isPresent()) {
            collection.remove(NAME_FILTER, name);
            CrawlState state = simulation.get().getState();
            state.drop();
        }
    }
}

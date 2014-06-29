package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbWebPagesRepositoryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoWebPagesRepository;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoSimulationRepositoryTest {
    private static final String A_URL = "http://localhost/";
    public static final String NAME = "name";
    @Mock
    private MongoDbWebPagesRepositoryFactory webPagesRepositoryFactory;
    @Mock
    private JongoConnectionFactory jongoConnectionFactory;
    private Map<String, MongoWebPagesRepository> mongoWebPagesRepositories;
    private MongoSimulationRepository mongoSimulationRepository;

    @Before
    public void setUp() throws Exception {
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        when(jongoConnectionFactory.create()).thenReturn(jongo);
        mongoSimulationRepository = new MongoSimulationRepository(jongoConnectionFactory, webPagesRepositoryFactory);
        mongoWebPagesRepositories = new HashMap<>();
        // Ensure each simulation returns the same web page repository
        when(webPagesRepositoryFactory.create(anyString()))
                .thenAnswer(args -> {
                    String name = (String) args.getArguments()[0];
                    return mongoWebPagesRepositories.computeIfAbsent(name, n -> mock(MongoWebPagesRepository.class));
                });
    }

    @Test
    public void shouldInsertADescriptor() {
        SimulationDescriptor descriptor = new SimulationDescriptor(NAME, A_URL, A_URL);
        Simulation simulation = createSimulation(descriptor);
        mongoSimulationRepository.add(simulation);

        Optional<Simulation> registeredSimulation = mongoSimulationRepository.get(NAME);
        assertThat(registeredSimulation.isPresent(), is(true));
        SimulationDescriptor result = registeredSimulation.get().getDescriptor();
        assertThat(result, is(descriptor));
    }

    private Simulation createSimulation(SimulationDescriptor descriptor) {
        throw new UnsupportedOperationException("Not yet implemented");
//        return new Simulation(descriptor, webPagesRepositoryFactory.create(descriptor.getName()), null);
    }

    @Test
    public void shouldDeleteDescriptor() {
        SimulationDescriptor descriptor = new SimulationDescriptor(NAME, A_URL, A_URL);
        Simulation simulation = createSimulation(descriptor);
        mongoSimulationRepository.add(simulation);

        mongoSimulationRepository.deleteByName(NAME);

        assertThat(mongoSimulationRepository.get(NAME).isPresent(), is(false));
        MongoWebPagesRepository webPagesRepository = (MongoWebPagesRepository) simulation.getWebPagesRepository();
        verify(webPagesRepository).drop();
    }
}
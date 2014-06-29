package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbCrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl.MongoDbCrawlStateFactory;
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
    public static final String NAME = "name";
    private static final String A_URL = "http://localhost/";
    @Mock
    private MongoDbCrawlStateFactory crawlStateFactory;
    @Mock
    private JongoConnectionFactory jongoConnectionFactory;
    private Map<String, MongoDbCrawlState> crawlStateMap;
    private MongoSimulationRepository mongoSimulationRepository;

    @Before
    public void setUp() throws Exception {
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        when(jongoConnectionFactory.create()).thenReturn(jongo);
        mongoSimulationRepository = new MongoSimulationRepository(jongoConnectionFactory, crawlStateFactory);
        crawlStateMap = new HashMap<>();
        // Ensure each simulation returns the same web page repository
        when(crawlStateFactory.create(anyString()))
                .thenAnswer(args -> {
                    String name = (String) args.getArguments()[0];
                    return crawlStateMap.computeIfAbsent(name, n -> mock(MongoDbCrawlState.class));
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
//        return new Simulation(descriptor, crawlStateFactory.create(descriptor.getName()), null);
    }

    @Test
    public void shouldDeleteDescriptor() {
        SimulationDescriptor descriptor = new SimulationDescriptor(NAME, A_URL, A_URL);
        Simulation simulation = createSimulation(descriptor);
        mongoSimulationRepository.add(simulation);

        mongoSimulationRepository.deleteByName(NAME);

        assertThat(mongoSimulationRepository.get(NAME).isPresent(), is(false));
        MongoDbCrawlState state = (MongoDbCrawlState) simulation.getState();
        verify(state).drop();
    }
}
package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.simulator;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoDbWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoWebPagesRegistry;
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
public class MongoSimulationRegistryTest {
    private static final String A_URL = "http://localhost/";
    public static final String NAME = "name";
    @Mock
    private MongoDbWebPagesRegistryFactory webPagesRegistryFactory;
    @Mock
    private JongoConnectionFactory jongoConnectionFactory;
    private Map<String, MongoWebPagesRegistry> mongoWebPagesRegistries;
    private MongoSimulationRegistry mongoSimulationRegistry;

    @Before
    public void setUp() throws Exception {
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        when(jongoConnectionFactory.create()).thenReturn(jongo);
        mongoSimulationRegistry = new MongoSimulationRegistry(jongoConnectionFactory, webPagesRegistryFactory);
        mongoWebPagesRegistries = new HashMap<>();
        // Ensure each simulation returns the same web page registry
        when(webPagesRegistryFactory.create(anyString()))
                .thenAnswer(args -> {
                    String name = (String) args.getArguments()[0];
                    return mongoWebPagesRegistries.computeIfAbsent(name, n -> mock(MongoWebPagesRegistry.class));
                });
    }

    @Test
    public void shouldInsertADescriptor() {
        SimulationDescriptor descriptor = new SimulationDescriptor(NAME, A_URL, A_URL);
        Simulation simulation = createSimulation(descriptor);
        mongoSimulationRegistry.register(simulation);

        Optional<Simulation> registeredSimulation = mongoSimulationRegistry.get(NAME);
        assertThat(registeredSimulation.isPresent(), is(true));
        SimulationDescriptor result = registeredSimulation.get().getDescriptor();
        assertThat(result, is(descriptor));
    }

    private Simulation createSimulation(SimulationDescriptor descriptor) {
        return new Simulation(descriptor, webPagesRegistryFactory.create(descriptor.getName()));
    }

    @Test
    public void shouldDeleteDescriptor() {
        SimulationDescriptor descriptor = new SimulationDescriptor(NAME, A_URL, A_URL);
        Simulation simulation = createSimulation(descriptor);
        mongoSimulationRegistry.register(simulation);

        mongoSimulationRegistry.deleteByName(NAME);

        assertThat(mongoSimulationRegistry.get(NAME).isPresent(), is(false));
        MongoWebPagesRegistry webPagesRegistry = (MongoWebPagesRegistry) simulation.getWebPagesRegistry();
        verify(webPagesRegistry).drop();
    }
}
package fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.simulator;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.registry.crawl.MongoDbWebPagesRegistryFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import org.jongo.Jongo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoDbSimulationRegistryTest {
    private static final String A_URL = "http://localhost/";
    public static final String NAME = "name";
    @Mock
    private MongoDbWebPagesRegistryFactory webPagesRegistryFactory;
    @Mock
    private JongoConnectionFactory jongoConnectionFactory;
    private MongoDbSimulationRegistry mongoDbSimulationRegistry;

    @Before
    public void setUp() throws Exception {
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        when(jongoConnectionFactory.create()).thenReturn(jongo);
        mongoDbSimulationRegistry = new MongoDbSimulationRegistry(jongoConnectionFactory, webPagesRegistryFactory);
    }

    @Test
    public void shouldInsertADescriptor() {
        SimulationDescriptor descriptor = new SimulationDescriptor(NAME, A_URL, A_URL);
        Simulation simulation = createSimulation(descriptor);
        mongoDbSimulationRegistry.register(simulation);

        Optional<Simulation> registeredSimulation = mongoDbSimulationRegistry.get(NAME);
        assertThat(registeredSimulation.isPresent(), is(true));
        SimulationDescriptor result = registeredSimulation.get().getDescriptor();
        assertThat(result, is(descriptor));
    }

    private Simulation createSimulation(SimulationDescriptor descriptor)
    {
        return new Simulation(descriptor, webPagesRegistryFactory.create(descriptor.getName()));
    }
}
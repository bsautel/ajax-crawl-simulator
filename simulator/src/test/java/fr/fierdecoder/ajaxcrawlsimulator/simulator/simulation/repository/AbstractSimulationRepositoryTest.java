package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.Simulation;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.SimulationDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractSimulationRepositoryTest {
    public static final String NAME = "name";
    private static final String A_URL = "http://localhost/";
    public static final SimulationDescriptor A_SIMULATION_DESCRIPTION = SimulationDescriptor.create(NAME, A_URL, A_URL);
    @Mock
    private CrawlStateFactory crawlStateFactory;
    private SimulationRepository simulationRepository;

    @Before
    public void setUp() throws Exception {
        crawlStateFactory = new MongoDbCrawlStateMockFactory();
        simulationRepository = createSimulationRepository(crawlStateFactory);
    }

    protected abstract SimulationRepository createSimulationRepository(CrawlStateFactory crawlStateFactory);

    @Test
    public void shouldInsertADescriptor() {
        Simulation simulation = createSimulation(A_SIMULATION_DESCRIPTION);
        simulationRepository.add(simulation);

        Optional<Simulation> registeredSimulation = simulationRepository.get(NAME);
        assertThat(registeredSimulation.isPresent(), is(true));
        SimulationDescriptor result = registeredSimulation.get().getDescriptor();
        assertThat(result, is(A_SIMULATION_DESCRIPTION));
    }

    private Simulation createSimulation(SimulationDescriptor descriptor) {
        return Simulation.create(descriptor, crawlStateFactory.create(descriptor.name()));
    }

    @Test
    public void shouldDeleteDescriptor() {
        Simulation simulation = createSimulation(A_SIMULATION_DESCRIPTION);
        simulationRepository.add(simulation);

        simulationRepository.deleteByName(NAME);

        assertThat(simulationRepository.get(NAME).isPresent(), is(false));
        CrawlState state = simulation.getState();
        verify(state).drop();
    }

    @Test
    public void shouldReturnDescriptors() {
        Simulation simulation = createSimulation(A_SIMULATION_DESCRIPTION);
        simulationRepository.add(simulation);

        SimulationDescriptor anotherSimulationDescription = SimulationDescriptor.create("another name", "url", "url");
        simulationRepository.add(createSimulation(anotherSimulationDescription));

        assertThat(simulationRepository.getDescriptors(), containsInAnyOrder(A_SIMULATION_DESCRIPTION,
                anotherSimulationDescription));
    }
}

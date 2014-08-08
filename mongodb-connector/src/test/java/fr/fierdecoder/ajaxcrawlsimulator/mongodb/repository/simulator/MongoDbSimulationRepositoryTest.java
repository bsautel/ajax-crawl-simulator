package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.simulator;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.mongodb.JongoConnectionFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.AbstractSimulationRepositoryTest;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;
import org.jongo.Jongo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MongoDbSimulationRepositoryTest extends AbstractSimulationRepositoryTest {
    @Override
    protected SimulationRepository createSimulationRepository(CrawlStateFactory crawlStateFactory) {
        DB db = new Fongo("Test").getDB("Test");
        Jongo jongo = new Jongo(db);
        JongoConnectionFactory jongoConnectionFactory = mock(JongoConnectionFactory.class);
        when(jongoConnectionFactory.create()).thenReturn(jongo);
        return new MongoDbSimulationRepository(jongoConnectionFactory, crawlStateFactory);
    }
}
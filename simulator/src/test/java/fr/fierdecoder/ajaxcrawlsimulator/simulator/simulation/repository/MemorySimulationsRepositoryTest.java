package fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;

public class MemorySimulationsRepositoryTest extends AbstractSimulationRepositoryTest {
    @Override
    protected SimulationRepository createSimulationRepository(CrawlStateFactory crawlStateFactory) {
        return new MemorySimulationsRepository();
    }
}
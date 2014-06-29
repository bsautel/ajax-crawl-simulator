package fr.fierdecoder.ajaxcrawlsimulator.mongodb.repository.crawl;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.AbstractCrawlStateTest;
import org.jongo.Jongo;

public class MongoDbCrawlStateTest extends AbstractCrawlStateTest {
    private final Jongo jongo;

    public MongoDbCrawlStateTest() {
        DB db = new Fongo("Test").getDB("Test");
        jongo = new Jongo(db);
    }

    @Override
    protected MongoDbCrawlState createCrawlState(String name) {
        return new MongoDbCrawlState(name, jongo);
    }
}

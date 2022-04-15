package data.external;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import config.Config;
import controller.Controller;
import data.external.neo4j.Neo4jConnection;

class neo4jClarissaTest {

	@Test
	void test() throws Exception {
		Config config = Config.of(new File("/home/clivings/Documents/EU_TRANSIT/Data/inputFiles_cvl_tests/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
     
	}

}

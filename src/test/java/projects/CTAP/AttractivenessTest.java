package projects.CTAP;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.graph.rail.gtfs.GTFS;
import projects.CTAP.attractiveness.normalized.Model;

class AttractivenessTest {

	@Test
	void attractivenessNormalizedTest() throws Exception {
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		projects.CTAP.attractiveness.normalized.Utils.insertAttractivenessNormalizedIntoNeo4j();
		//System.out.println();
	}
	
	@Test
	void deleteAttractivenessTest() throws Exception {
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		projects.CTAP.attractiveness.normalized.Utils.deleteAttractivenessLinks();
		System.out.println();
	}

}

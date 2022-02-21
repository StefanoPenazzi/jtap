package projects.CTAP;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.graph.rail.gtfs.GTFS;
import projects.CTAP.attractiveness.AttractivenessCTAP;

class AttractivenessTest {

	@Test
	void attractivenessNormalizedTest() throws Exception {
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		projects.CTAP.attractiveness.Utils.attractivenessNormalized(250000,0,8760,336,Controller.getInjector().getInstance(AttractivenessCTAP.class));
		System.out.println();
	}
	
	@Test
	void deleteAttractivenessTest() throws Exception {
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		projects.CTAP.attractiveness.Utils.deleteAttractivenessLinks();
		System.out.println();
	}

}

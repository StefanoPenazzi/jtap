package core.graph.facility.osm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.geo.CityNode;

class UtilityTest {

	@Test
	void neo4jOSMFacilityTest() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		core.graph.geo.Utils.addCityFacStatNode();
		
	}

}

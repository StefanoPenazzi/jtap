package core.graph.air;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadNode;

class UtilsTest {

	@Test
	void test() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		core.graph.air.Utils.insertAirNetworkNeo4j();
		
		//air connections 
		Map<Class<? extends NodeGeoI>,String> airConnMap = new HashMap<>();
		airConnMap.put(RoadNode.class,"node_osm_id");
		airConnMap.put(RailNode.class,"stop_id");
		core.graph.Utils.setShortestDistCrossLink(AirNode.class,"airport_id",airConnMap,3);
				
	
	}

}

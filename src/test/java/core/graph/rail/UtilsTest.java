package core.graph.rail;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.geo.City;
import core.graph.rail.gtfs.GTFS;
import core.graph.rail.gtfs.Stop;
import core.graph.road.osm.RoadNode;

class UtilsTest {

	@Test
	void insertRailGTFSintoNeo4JTest() throws Exception {
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		String db = "france2";
		
		//insert GTFS
		GTFS gtfs = controller.getInjector().getInstance(GTFS.class);
		core.graph.rail.Utils.deleteRailGTFS(db);
		core.graph.rail.Utils.insertRailGTFSintoNeo4J(gtfs,db,controller.getInjector().getInstance(Config.class));
		
		//connections between subgraphs
		Map<Class<? extends NodeGeoI>,String> railConnMap = new HashMap<>();
		railConnMap.put(RoadNode.class,"node_osm_id");
		core.graph.Utils.setShortestDistCrossLink(db, config.getGeneralConfig().getTempDirectory(),Stop.class,"id",railConnMap,true);
		
		Map<Class<? extends NodeGeoI>,String> cityConnMap = new HashMap<>();
		cityConnMap.put(Stop.class, "id");
		core.graph.Utils.setShortestDistCrossLink(db, config.getGeneralConfig().getTempDirectory(),City.class,"city",cityConnMap,true);
		
	}

}

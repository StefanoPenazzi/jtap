package core.graph.geo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.rail.gtfs.GTFS;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadNode;


class UtilsTest {

	@Test
	void citiesTest() throws Exception {
		
		Config config = Config.of(new File("/home/stefanopenazzi/projects/jtap/config_.xml")); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		String db = "france2";
		
		//insert cities
		core.graph.geo.Utils.deleteCities(db);
		
		//insert cities
		core.graph.geo.Utils.insertCitiesIntoNeo4JFromCsv(db,controller.getInjector().getInstance(Config.class),CityNode.class);

		//connect cities
		Map<Class<? extends NodeGeoI>,String> cityConnMap = new HashMap<>();
		cityConnMap.put(RoadNode.class,"node_osm_id");
		cityConnMap.put(RailNode.class, "id");
		core.graph.Utils.setShortestDistCrossLink(db, config.getGeneralConfig().getTempDirectory(),CityNode.class,"city",cityConnMap,3);
		
		
	}

}

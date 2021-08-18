package core.graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;

class AlgoTest {

	@Test
	void ShortestPathAStarTest() throws Exception {
		List<Record> rec = core.graph.Utils.getShortestPathAStar("france2","intersections-graph", "RoadNode", 
				"node_osm_id", "1012744632", "RoadNode","node_osm_id", "3282509284", "lat", "lon", "avg_travel_time", "node_osm_id");
		System.out.println();
	}

}

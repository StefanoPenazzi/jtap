package core.graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;

class AlgoTest {

	@Test
	void ShortestPathAStarTest() throws Exception {
		List<Record> rec = core.graph.Utils.getShortestPathAStar("osm","train-city-intersections-graph", "CityNode", 
				"city", "Nancy", "CityNode","city", "Laval", "lat", "lon", "avg_travel_time", "nodeId");
		System.out.println();
	}

}

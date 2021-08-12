package core.graph;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;
import data.external.neo4j.Neo4jConnection;

class UtilsTest {

	@Test
	void shortestPathAStarTest() throws Exception {
		List<Record> res = core.graph.Utils.getShortestPathAStar("osm", "train-intersections-graph", "RailNode",
				"stop_id", "StopPoint:OCECar TER-87117994", "RailNode", "stop_id",
				"StopPoint:OCECar TER-87117952", "lat", "lon", "avg_travel_time",
				"stop_id");
		System.out.println();
	}
	
	

}

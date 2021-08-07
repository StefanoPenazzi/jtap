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
	
	@Test
	void createGraphCatalogTest() throws Exception {
		Neo4jConnection conn = new Neo4jConnection();
		
		core.graph.Utils.createGraphCatalog(conn,"osm",
				"train-intersections-graph",
				"MATCH (n) WHERE n:RailNode RETURN id(n) AS id, labels(n) AS labels, n.stop_lat AS lat, n.stop_lon AS lon",
				"MATCH (n)-[r:RailLink|RailTransferLink|RailIntersectionLink]->(m) WHERE (n:RailNode) AND (m:RailNode) RETURN id(n) AS source, id(m) AS target, type(r) AS type, r.avg_travel_time AS avg_travel_time");
		System.out.println();
		conn.close();
	}
	
	@Test
	void getCatalogTest() throws Exception {
		Neo4jConnection conn = new Neo4jConnection();
		List<Record> res = core.graph.Utils.getCatalog(conn,"osm");
		System.out.println();
		conn.close();
	}
	
	@Test
	void deleteFromCatalogTest() throws Exception {
		Neo4jConnection conn = new Neo4jConnection();
		core.graph.Utils.deleteGraphCatalog(conn,"osm","train-intersections-graph");
		System.out.println();
		conn.close();
	}

}

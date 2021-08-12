package data.external;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;

import data.external.neo4j.Neo4jConnection;

class UtilsTest {

	@Test
	void createGraphCatalogTest() throws Exception {
		Neo4jConnection conn = new Neo4jConnection();
		
		data.external.neo4j.Utils.createGraphCatalog(conn,"osm",
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
		data.external.neo4j.Utils.deleteGraphCatalog(conn,"osm","train-intersections-graph");
		System.out.println();
		conn.close();
	}

}

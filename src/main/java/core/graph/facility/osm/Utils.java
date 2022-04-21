package core.graph.facility.osm;

import org.neo4j.driver.AccessMode;

import config.Config;
import controller.Controller;
import core.graph.geo.CityNode;
import data.external.neo4j.Neo4jConnection;

public class Utils {
	
	public static void facilitiesIntoNeo4j(Config config) throws Exception {
		String database = config.getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"MATCH (x:OSMNode)-[:TAGS]->(t:OSMTags)"
					+ "  WHERE (exists(t.amenity) OR exists(t.tourism)) AND NOT (x)-[:ROUTE]->() set x:FacilityNode return count(x)",AccessMode.WRITE);
			conn.query(database,"CREATE INDEX FacilityNodeIndex FOR (n:FacilityNode) ON (n.node_osm_id)",AccessMode.WRITE);
			
		}
	}
	
	public static void deleteFacilities() throws Exception {
		try(Neo4jConnection conn = Controller.getNeo4JConnection()){ 
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:FacilityNode)-[r:CrossLink]->(m:CityNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:FacilityNode) RETURN n limit 10000000\", \"remove n:FacilityNode\",{batchSize:100000});",AccessMode.WRITE );
			conn.query("DROP INDEX FacilityNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 
}

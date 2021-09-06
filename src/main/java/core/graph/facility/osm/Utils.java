package core.graph.facility.osm;

import org.neo4j.driver.AccessMode;

import config.Config;
import core.graph.geo.City;
import data.external.neo4j.Neo4jConnection;

public class Utils {
	
	public static void facilitiesIntoNeo4j(String database) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"MATCH (x:OSMNode)-[:TAGS]->(t:OSMTags)"
					+ "  WHERE (exists(t.amenity) OR exists(t.tourism)) AND NOT (x)-[:ROUTE]->() set x:FacilityNode return count(x)",AccessMode.WRITE);
			conn.query(database,"CREATE INDEX FacilityNodeIndex FOR (n:FacilityNode) ON (n.node_osm_id)",AccessMode.WRITE);
			
		}
	}
}

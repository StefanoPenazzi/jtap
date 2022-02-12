package core.graph.road.osm;

import org.neo4j.driver.AccessMode;

import config.Config;
import controller.Controller;
import data.external.neo4j.Neo4jConnection;

public class Utils {
	
	public static void insertOSMRoadNetworkIntoNeo4j(String neo4jInstallationDir, String database, String osmFile) {
//		ProcessBuilder pb = new ProcessBuilder("/path/to/java", "-jar", "your.jar");
//		pb.directory(new File("preferred/working/directory"));
//		Process p = pb.start();
	}
	
	public static void setOSMRoadNetworkIntoNeo4j() throws Exception {
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = new Neo4jConnection()){  
			String s = "MATCH (n:OSMNode)\n"
					+ "  WHERE size((n)<-[:NODE]-(:OSMWayNode)-[:NEXT]-(:OSMWayNode)) > 2\n"
					+ "  AND NOT (n:Intersection)\n"
					+ "WITH n LIMIT 10000000 \n"
					+ "MATCH (n)<-[:NODE]-(wn:OSMWayNode), (wn)<-[:NEXT*0..100]-(wx),\n"
					+ "      (wx)<-[:FIRST_NODE]-(w:OSMWay)-[:TAGS]->(wt:OSMTags)\n"
					+ "  WHERE exists(wt.highway)\n"
					+ "SET n:Intersection:RoadNode\n"
					+ "SET n.maxspeed = CASE\n"
					+ "  WHEN exists(wt.maxspeed) THEN wt.maxspeed\n"
					+ "  ELSE 50\n"
					+ "END\n"
					+ "RETURN count(*);";
			//
			conn.query(database,s,AccessMode.WRITE);
			
			conn.query(database,"MATCH(awn:OSMWayNode)-[r:NEXT]-(bwn:OSMWayNode)\n"
					+ "  WHERE NOT exists(r.distance)\n"
					+ "WITH awn,bwn,r LIMIT 10000000 \n"
					+ "MATCH (awn)-[:NODE]->(a:OSMNode),  (bwn)-[:NODE]->(b:OSMNode)\n"
					+ "SET r.distance=distance(a.location,b.location)\n"
					+ "RETURN COUNT(*);",AccessMode.WRITE);
			
			conn.query(database,"MATCH (x:Intersection) WITH x LIMIT 10000000\n"
					+ "  CALL spatial.osm.routeIntersection(x,false,false,false)\n"
					+ "  YIELD fromNode, toNode, fromRel, toRel, distance, length, count\n"
					+ "WITH fromNode, toNode, fromRel, toRel, distance, length, count\n"
					+ "MERGE (fromNode)-[r:RoadLink {fromRel:id(fromRel),toRel:id(toRel)}]->(toNode)\n"
					+ "  ON CREATE SET r.distance = distance, r.length = length, r.count = count\n"
					+ "RETURN count(*);",AccessMode.WRITE);
			
			conn.query(database,"Match (n:RoadNode)-[r:RoadLink]->(m:RoadNode) set r.avg_travel_time=toFloat(r.distance)/(toFloat(n.maxspeed)*1000/3600)",AccessMode.WRITE);
			conn.query(database,"MATCH (n:RoadNode)-[r:RoadLink]->(m:RoadNode) where not exists(r.avg_travel_time) set r.avg_travel_time=toInteger(r.distance/13.9)",AccessMode.WRITE);
			
			conn.query(database,"CREATE INDEX RoadNodeIndex FOR (n:RoadNode) ON (n.node_osm_id)",AccessMode.WRITE);
			
		}
	}

}

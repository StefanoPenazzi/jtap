package core.graph.road.osm;

import org.neo4j.driver.AccessMode;

import data.external.neo4j.Neo4jConnection;

/**
 * @author stefanopenazzi
 * 
 * This class is meant to provide static methods to generate a routable road network using OSM data into Neo4J.
 *
 */
public class Utils {
	
	/**
	 * <p>
	 * <font color="red"> 
	 * Before using this method is necessary to add the OSM model in the database
	 *  using the following repository See <a href="https://github.com/neo4j-contrib/osm"> neo4j contrib/osm </a> .
	 *  The road network model reflects the OSM input file. This means that the 
	 *  detail level depends from OSM input file. A tutorial step by step to create
	 *  the road network model in Neo4j with different levels of detail is also provided here... 
	 * </font>
	 * </p>
	 *  
	 * 
	 * <p>Nodes that belong to the road network are labeled with RoadNode and Intersection.
	 *  This latter is required by the spatial.osm.routeIntersection method that belongs to 
	 *  See <a href="https://github.com/neo4j-contrib/osm"> neo4j contrib/osm </a> . 
	 *  These can be installed into an installation of Neo4j by copying the osm-0.2.3-neo4j-4.1.3-procedures.jar
	 *  file into the plugins folder, and restarting the database.
	 *  A tutorial step by step to perform this task is also provided here...
	 *  </p>
	 *  
	 *  <p> The links between two nodes are labeled with RoadLink. The link's properties are 
	 *  distance [m], lenght, count, avg_travel_time [s] </p>
	 *  
	 *  <p> The RoadNodes are indexed using the node_osm_id property. This make much more
	 *  faster the later queries using node_osm_id to find a specific node </p>
	 *  
	 * 
	 * @param database the name of the db in which run the the query
	 * @throws Exception
	 */
	public static void setOSMRoadNetworkIntoNeo4j(String database) throws Exception {
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
			
			//For some reason a few RoadLinks (0.2%) do not get the property avg_trave_time at the previous step. 
			//all of get an avg_travel_time equal to distance[m]/13.9[m/s]
			conn.query(database,"MATCH (n:RoadNode)-[r:RoadLink]->(m:RoadNode) where not exists(r.avg_travel_time) set r.avg_travel_time=toInteger(r.distance/13.9)",AccessMode.WRITE);
			
			conn.query(database,"CREATE INDEX RoadNodeIndex IF NOT EXISTS FOR (n:RoadNode) ON (n.node_osm_id)",AccessMode.WRITE);
			
		}
	}

}

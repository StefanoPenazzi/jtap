package core.graph.geo;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AccessMode;

import config.Config;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.rail.gtfs.GTFS;
import data.external.neo4j.Neo4jConnection;
import data.utils.io.CSV;

public class Utils {
	
	public static <T extends CityNode> void insertCitiesIntoNeo4JFromCsv(Class<T> cityClass) throws Exception {
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		core.graph.Utils.insertNodesIntoNeo4J(database,config.getDbScenarioConfig().getGeoLocConfig().getCitiesFile(),config.getGeneralConfig().getTempDirectory(),cityClass);
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"CREATE INDEX CityNodeIndex FOR (n:CityNode) ON (n.city_id)",AccessMode.WRITE);
		}
	}
	
	public static <T extends CityNode> void insertAndConnectCitiesIntoNeo4JFromCsv(Config config,Class<T> cityClass,Map<Class<? extends NodeGeoI>,String> nodeArrivalMap) throws Exception {
		String database = config.getNeo4JConfig().getDatabase();
		insertCitiesIntoNeo4JFromCsv(cityClass);
		core.graph.Utils.setShortestDistCrossLink(cityClass,"id", nodeArrivalMap,3);
	}
	
	/**
	 * @param database
	 * @throws Exception
	 * 
	 * The queries in this method consider the fact that the FacilityNodes are already connected with the CityNodes. 
	 * FacilityNodes can be connected to CityNodes using core.graph.facility.osm.Utils.facilitiesIntoNeo4j(db) <br>
	 * 
	 * The method add new nodes labeled with CityFacStatNode containing a map of useful information for the attractiveness 
	 * of the city. The CityFacStatNode are connected with CityNodes through the relation labeled with STAT <br>
	 * 
	 * <font color="red"> This method can take some time in case the FacilityNodes are many </font>
	 * 
	 */
	public static void addCityFacStatNode() throws Exception {
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"MATCH (cit:CityNode) WITH DISTINCT cit.city_id AS cities UNWIND cities AS cn CREATE (g:CityFacStatNode {city_id:cn}) With g,cn Match(cit:CityNode) where cit.city_id=cn create(cit)-[j:STAT]->(g);",AccessMode.WRITE);
			conn.query(database,"match (n:CityNode)<-[r:CrossLink]-(m:FacilityNode)-[k:TAGS]->(f:OSMTags) \n"
					+ "WITH DISTINCT f.tourism AS tourism,n.city_id AS cn,f AS ot UNWIND tourism AS tt WITH count(ot.tourism = tt) AS c,tt,cn MATCH (g:CityFacStatNode) WHERE g.city_id=cn  CALL apoc.create.setProperty(g,tt,c) YIELD node\n"
					+ "RETURN count(*);",AccessMode.WRITE);
			conn.query(database,"match (n:CityNode)<-[r:CrossLink]-(m:FacilityNode)-[k:TAGS]->(f:OSMTags) \n"
					+ "WITH DISTINCT f.amenity AS amenities,n.city_id AS cn,f AS ot UNWIND amenities AS tt WITH count(ot.amenity = tt) AS c,tt,cn MATCH (g:CityFacStatNode) WHERE g.city_id=cn  CALL apoc.create.setProperty(g,tt,c) YIELD node\n"
					+ "RETURN count(*); ",AccessMode.WRITE);
		}
	}
	
	public static void deleteCities() throws Exception {
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:CityNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:CityNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:CityNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX CityNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 
}

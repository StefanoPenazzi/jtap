package core.graph.geo;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AccessMode;

import config.Config;
import core.graph.NodeGeoI;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.rail.gtfs.GTFS;
import data.external.neo4j.Neo4jConnection;
import data.utils.io.CSV;

public class Utils {
	
	public static <T extends City> void insertCitiesIntoNeo4JFromCsv(String database,Config config,Class<T> cityClass) throws Exception {
		core.graph.Utils.insertNodesIntoNeo4J(database,config.getGeoLocConfig().getCitiesFile(),config.getGeneralConfig().getTempDirectory(),cityClass);
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"CREATE INDEX CityNodeIndex FOR (n:CityNode) ON (n.city)",AccessMode.WRITE);
		}
	}
	
	public static <T extends City> void insertAndConnectCitiesIntoNeo4JFromCsv(String database,Config config,Class<T> cityClass,Map<Class<? extends NodeGeoI>,String> nodeArrivalMap) throws Exception {
		insertCitiesIntoNeo4JFromCsv(database,config,cityClass);
		core.graph.Utils.setShortestDistCrossLink(database, config.getGeneralConfig().getTempDirectory(),cityClass,"id", nodeArrivalMap,true);
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
	public static void addCityFacStatNode(String database) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"MATCH (cit:CityNode) WITH DISTINCT cit.city AS cities UNWIND cities AS cn CREATE (g:CityFacStatNode {city:cn}) With g,cn Match(cit:CityNode) where cit.city=cn create(cit)-[j:STAT]->(g);",AccessMode.WRITE);
			conn.query(database,"match (n:CityNode)-[r:CrossLink]->(m:FacilityNode)-[k:TAGS]->(f:OSMTags) \n"
					+ "WITH DISTINCT f.tourism AS tourism,n.city AS cn,f AS ot UNWIND tourism AS tt WITH count(ot.tourism = tt) AS c,tt,cn MATCH (g:CityFacStatNode) WHERE g.city=cn  CALL apoc.create.setProperty(g,tt,c) YIELD node\n"
					+ "RETURN count(*);",AccessMode.WRITE);
			conn.query(database,"match (n:CityNode)-[r:CrossLink]->(m:FacilityNode)-[k:TAGS]->(f:OSMTags) \n"
					+ "WITH DISTINCT f.amenity AS amenities,n.city AS cn,f AS ot UNWIND amenities AS tt WITH count(ot.amenity = tt) AS c,tt,cn MATCH (g:CityFacStatNode) WHERE g.city=cn  CALL apoc.create.setProperty(g,tt,c) YIELD node\n"
					+ "RETURN count(*); ",AccessMode.WRITE);
		}
	}
	
	public static void deleteCities(String database) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:CityNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:CityNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:CityNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX CityNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 
}

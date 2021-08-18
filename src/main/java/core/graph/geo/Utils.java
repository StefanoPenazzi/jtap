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

}

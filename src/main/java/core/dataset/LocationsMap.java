package core.dataset;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeI;
import data.external.neo4j.Neo4jConnection;

public class LocationsMap <T extends NodeI> implements DatasetMapI {
	
	List<T> locations  = new ArrayList<>();
	private Config config;
	
	@Inject
	public LocationsMap(Config config) {
		this.config = config; 
	}
	
	public void getLocationsFromNeo4J(Class<T> agentClass) throws Exception {
		locations = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),agentClass);
		
    	String query = "match (n:AgentNode)-[r]->(m:CityNode) return n,r,m";
    	 
    	List<Record> records = data.external.neo4j.Utils.runQuery("france2",query,AccessMode.READ);
    
	    System.out.println("");
		
	}

}

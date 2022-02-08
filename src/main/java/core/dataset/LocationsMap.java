package core.dataset;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeI;

public class LocationsMap <T extends NodeI> implements DatasetMapI {
	
	List<T> locations  = new ArrayList<>();
	private Config config;
	
	@Inject
	public LocationsMap(Config config) {
		this.config = config; 
	}
	
	public void getLocationsFromNeo4J(Class<T> agentClass) throws Exception {
		locations = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),agentClass);
	}

}

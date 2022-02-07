package core.dataset;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeI;

public class AgentsMap <T extends NodeI> implements DatasetMapI {
	
	List<T> agents  = new ArrayList<>();
	private Config config;
	
	@Inject
	public AgentsMap(Config config) {
		this.config = config; 
	}
	
	public void getAgentsFromNeo4J(Class<T> agentClass) throws Exception {
		agents = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),agentClass);
	}
}

package core.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeI;
import core.graph.population.AgentI;

public class AgentsMap <T extends AgentI> implements DatasetMapI {
	
	Map<Integer,T> agentsMap  = new HashMap<>();
	private Config config;
	
	@Inject
	public AgentsMap(Config config) {
		this.config = config; 
	}
	
	public void getAgentsFromNeo4J(Class<T> agentClass) throws Exception {
		List<T> agents = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),agentClass);
		for(T agent: agents) {
			agentsMap.put(agent.getId(), agent);
		}
	}
}

package core.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeI;
import core.graph.population.AgentNodeI;
import core.graph.population.StdAgentNodeImpl;

public class AgentsMap <T extends AgentNodeI> implements DatasetMapI {
	
	protected Map<Integer,T> agentsMap  = new ConcurrentHashMap<>();
	private Config config;
	public static final String AGENT_MAP_KEY = "AgentsMap";
	
	
	public AgentsMap(Config config) {
		this.config = config; 
	}
	
	public void getAgentsFromNeo4J(Class<T> agentClass) throws Exception {
		List<T> agents = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),agentClass);
		for(T agent: agents) {
			agentsMap.put(agent.getId(), agent);
		}
	}

	@Override
	public String getKey() {
		return AGENT_MAP_KEY;
	}

	@Override
	public void initialization() {
		
	}
}

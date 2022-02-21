package projects.CTAP.dataset;

import java.util.List;

import com.google.inject.Inject;

import config.Config;
import core.dataset.AgentsMap;
import core.graph.population.AgentNodeI;
import core.graph.population.StdAgentNodeImpl;

public class AgentsMapCTAP extends AgentsMap<StdAgentNodeImpl> {
 
	Config config;
	
	@Inject
	public AgentsMapCTAP(Config config) {
		super(config);
		this.config = config;
	}
	
	@Override
	public void initialization() {
		List<StdAgentNodeImpl> agents = null;
		try {
			agents = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),StdAgentNodeImpl.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(StdAgentNodeImpl agent: agents) {
			super.agentsMap.put(agent.getId(),agent);
		}
	}

}

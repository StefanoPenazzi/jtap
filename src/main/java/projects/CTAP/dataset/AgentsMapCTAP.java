package projects.CTAP.dataset;

import java.util.List;

import com.google.inject.Inject;

import config.Config;
import core.dataset.AgentsMap;
import core.graph.population.AgentI;
import core.graph.population.StdAgentImpl;

public class AgentsMapCTAP extends AgentsMap<StdAgentImpl> {
 
	Config config;
	
	@Inject
	public AgentsMapCTAP(Config config) {
		super(config);
		this.config = config;
	}
	
	@Override
	public void initialization() {
		List<StdAgentImpl> agents = null;
		try {
			agents = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),StdAgentImpl.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(StdAgentImpl agent: agents) {
			super.agentsMap.put(agent.getId(),agent);
		}
	}

}

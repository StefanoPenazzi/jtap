package core.dataset;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeI;

public class AgentsMap implements DatasetMapI {
	
	List<NodeI> agents  = new ArrayList<>();
	private Config config;
	
	@Inject
	public AgentsMap(Config config) {
		this.config = config;
	}
	
	public void getAgentsFromNeo4J() {
		
	}
	
	//import the agents from the DB
	
	//select agent
	
	//create random agent

}

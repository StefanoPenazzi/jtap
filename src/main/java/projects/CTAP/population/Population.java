package projects.CTAP.population;

import java.util.Iterator;
import java.util.List;

import core.population.PopulationI;

public final class Population implements PopulationI {
	
	private final List<Agent> agents;
	
	public Population(List<Agent> agents) {
		this.agents = agents;
	}
	
	public Iterator getAgentsIterator() {
		return this.agents.iterator();
	}
	
	public List<Agent> getAgents(){
		return this.agents;
	}

}

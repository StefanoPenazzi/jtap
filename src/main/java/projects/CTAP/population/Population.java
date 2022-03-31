package projects.CTAP.population;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Controller;
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
	
	public void save() {
		ObjectMapper mapper = new ObjectMapper();
    	try (Writer writer = new FileWriter(Controller.getConfig().getGeneralConfig().getOutputDirectory()+"population.json")) {
    	     writer.append(mapper.writeValueAsString(this));
    	} catch (IOException ex) {
    	  ex.printStackTrace(System.err);
    	}
	}

}

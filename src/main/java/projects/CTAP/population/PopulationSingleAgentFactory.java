package projects.CTAP.population;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import controller.Controller;
import core.dataset.DatasetI;
import core.population.AgentFactoryI;
import core.population.PopulationFactoryI;
import core.population.PopulationI;
import projects.CTAP.dataset.Dataset;

public class PopulationSingleAgentFactory implements PopulationFactoryI {
	
	private Config config;
	
	@Inject
	public PopulationSingleAgentFactory(Config config) {
		this.config = config;
	}

	@Override
	public PopulationI run(DatasetI ds_) {
		
		Dataset ds = (Dataset)ds_;
		Integer planSize = config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getPlanSize();
		List<Agent> agents = new ArrayList<>();
		AgentFactoryI af = Controller.getInjector().getInstance(AgentFactoryI.class);
		agents.add((Agent) af.run(ds.getAgentsIndex().getIndex().get(0),ds.getCitiesOsIndex().getIndex().get(0),ds));
		return new Population(agents);
	}

}


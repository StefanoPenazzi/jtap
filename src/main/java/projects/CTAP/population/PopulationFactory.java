package projects.CTAP.population;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import controller.Controller;
import core.dataset.DatasetFactoryI;
import core.dataset.DatasetI;
import core.population.AgentFactoryI;
import core.population.PopulationFactoryI;
import core.population.PopulationI;
import projects.CTAP.dataset.Dataset;
import projects.CTAP.dataset.DatasetJsonFactory;

public class PopulationFactory implements PopulationFactoryI {
	
	private Config config;
	
	@Inject
	public PopulationFactory(Config config) {
		this.config = config;
	}

	@Override
	public PopulationI run(DatasetI ds_) {
		
		Dataset ds = (Dataset)ds_;
		Integer planSize = config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getPlanSize();
		List<Agent> agents = new ArrayList<>();
		for(Long agId: ds.getAgentsIndex().getIndex()) {
			for(Long locId: ds.getCitiesOsIndex().getIndex()) {
				AgentFactoryI af = Controller.getInjector().getInstance(AgentFactoryI.class);
				agents.add((Agent) af.run(agId,locId,ds));
			}
			//for(Long locId: ds.getCitiesDsIndex().getIndex()) {
			//	AgentFactoryI af = Controller.getInjector().getInstance(AgentFactoryI.class);
			//	agents.add((Agent) af.run(agId,locId,ds));
			//}
		}
		return new Population(agents);
	}

}

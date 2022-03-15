package projects.CTAP.population;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import controller.Controller;
import core.dataset.DatasetFactoryI;
import core.population.AgentFactoryI;
import core.population.PopulationFactoryI;
import projects.CTAP.dataset.Dataset;
import projects.CTAP.dataset.DatasetJsonFactory;

public class PopulationFactory implements PopulationFactoryI {
	
	private Config config;
	private DatasetFactoryI datasetFactory;
	
	@Inject
	public PopulationFactory(Config config, DatasetFactoryI datasetFactory) {
		this.config = config;
		this.datasetFactory = datasetFactory;
	}
	
	@Override
	public Population run() {
		Dataset ds = (Dataset) datasetFactory.run();
		Integer planSize = config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getPlanSize();
		List<Agent> agents = new ArrayList<>();
		for(Long agId: ds.getAgentsIndex().getIndex()) {
			for(Long locId: ds.getCitiesOsIndex().getIndex()) {
				AgentFactoryI af = Controller.getInjector().getInstance(AgentFactory.class);
				agents.add((Agent) af.run(agId,locId,ds));
			}
		}
		return new Population(agents);
	}

}

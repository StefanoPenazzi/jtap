package projects.CTAP.population;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import config.Config;
import core.population.PopulationFactoryI;
import projects.CTAP.dataset.Dataset;
import projects.CTAP.dataset.DatasetJsonFactory;

public class PopulationFactory implements PopulationFactoryI {
	
	private Config config;
	
	@Inject
	public PopulationFactory(Config config) {
		this.config = config;
	}
	
	@Override
	public Population run() {
		DatasetJsonFactory dsf = new DatasetJsonFactory();
		Dataset ds = dsf.run();
		Integer planSize = config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getPlanSize();
		List<Agent> agents = new ArrayList<>();
		for(Long agId: ds.getAgentsIndex().getIndex()) {
			for(Long locId: ds.getCitiesOsIndex().getIndex()) {
				AgentFactory af = new AgentFactory(agId,locId,planSize,ds);
				agents.add(af.run());
			}
		}
		return new Population(agents);
	}

}

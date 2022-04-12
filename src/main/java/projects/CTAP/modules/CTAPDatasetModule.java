package projects.CTAP.modules;

import controller.AbstractModule;
import core.dataset.DatasetFactoryI;
import core.population.AgentFactoryI;
import core.population.PopulationFactoryI;
import projects.CTAP.attractiveness.AttractivenessModelI;
import projects.CTAP.attractiveness.normalized.DefaultAttractivenessModelImpl;
import projects.CTAP.dataset.DatasetJsonFactory;
import projects.CTAP.model.ActivityLocationI;
import projects.CTAP.model.RandomEvenHomeActivityLocation;
import projects.CTAP.population.AgentFactory;
import projects.CTAP.population.PopulationFactory;

public class CTAPDatasetModule extends AbstractModule{

	@Override
	public void install() {
		
		 binder().bind(PopulationFactoryI.class).to(PopulationFactory.class);
		 binder().bind(DatasetFactoryI.class).to(DatasetJsonFactory.class);
		 binder().bind(AgentFactoryI.class).to(AgentFactory.class);
		 binder().bind(ActivityLocationI.class).to(RandomEvenHomeActivityLocation.class);
		 binder().bind(AttractivenessModelI.class).to(DefaultAttractivenessModelImpl.class);
		 
		
	}
}

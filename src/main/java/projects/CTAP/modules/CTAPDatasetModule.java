package projects.CTAP.modules;

import controller.AbstractModule;
import core.dataset.AgentsMap;
import core.dataset.DatasetProvider;
import core.dataset.LocationsMap;
import core.dataset.RoutesMap;
import projects.CTAP.dataset.AgentsMapCTAP;
import projects.CTAP.dataset.LocationsMapCTAP;
import projects.CTAP.dataset.RoutesMapCTAP;

public class CTAPDatasetModule extends AbstractModule{

	@Override
	public void install() {
		addDatasetMap(AgentsMap.AGENT_MAP_KEY, AgentsMapCTAP.class);
		addDatasetMap(LocationsMap.LOCATIONS_MAP_KEY,LocationsMapCTAP.class);
		addDatasetMap(RoutesMap.ROUTES_MAP_KEY,RoutesMapCTAP.class);
		bindDataset(DatasetProvider.class);
	}
}

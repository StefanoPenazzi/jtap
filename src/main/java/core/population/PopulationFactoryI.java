package core.population;

import core.dataset.DatasetI;

public interface PopulationFactoryI {
	
	public PopulationI run(DatasetI ds);

}

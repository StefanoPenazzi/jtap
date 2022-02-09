package core.dataset;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DatasetProvider implements Provider<DatasetI>{
	
	@Inject Map<String,DatasetMapI> dsMap;

	@Override
	public DatasetI get() {
		for(DatasetMapI dmi:dsMap.values()) {
			dmi.initialization();
		}
		return new Dataset(dsMap);
	}
	
	
	
}

package core.dataset;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

public final class Dataset implements DatasetI {
	
	Map<String,DatasetMapI> ds;
	
	public Dataset(Map<String,DatasetMapI> ds) {
		this.ds = ds;
	}
	
	public DatasetMapI getMap(String key) {
		return ds.get(key);
	}
}

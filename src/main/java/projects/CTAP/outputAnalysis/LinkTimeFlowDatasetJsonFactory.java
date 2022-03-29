package projects.CTAP.outputAnalysis;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import config.Config;
import core.dataset.DatasetFactoryI;
import projects.CTAP.dataset.CitiesDsIndex;
import projects.CTAP.dataset.CitiesOsIndex;
import projects.CTAP.dataset.Ds2DsPathParameter;
import projects.CTAP.dataset.Ds2OsPathParameter;
import projects.CTAP.dataset.Os2DsPathParameter;

public class LinkTimeFlowDatasetJsonFactory implements DatasetFactoryI {
	
    private Config config;
	
    @Inject
	public LinkTimeFlowDatasetJsonFactory(Config config) {
		this.config = config;
	}

	@Override
	public LinkTimeFlowDataset run() {
		ObjectMapper mapper = new ObjectMapper();
		LinkTimeFlowDataset dataset = null;
	    String dir = config.getCtapModelConfig().getDatasetConfig().getImportDirectory();
	    try {
	    	
	    	CitiesDsIndex citiesDsIndex = mapper.readValue(new File(dir+"CitiesDsIndex.json"), CitiesDsIndex.class);
			CitiesOsIndex citiesOsIndex = mapper.readValue(new File(dir+"CitiesOsIndex.json"), CitiesOsIndex.class);
	    	
	    	Ds2OsPathParameter ds2OsPathParameter =  mapper.readValue(new File(dir+"Ds2OsPathParameter.json"), Ds2OsPathParameter.class);
	    	Ds2DsPathParameter ds2DsPathParameter =  mapper.readValue(new File(dir+"Ds2DsPathParameter.json"), Ds2DsPathParameter.class);
	    	Os2DsPathParameter os2DsPathParameter =  mapper.readValue(new File(dir+"Os2DsPathParameter.json"), Os2DsPathParameter.class);
	    	
	    	dataset = new LinkTimeFlowDataset(citiesDsIndex,
												citiesOsIndex,
												ds2OsPathParameter, 
												ds2DsPathParameter, 
												os2DsPathParameter);
	    }
	    catch (IOException e) {
		   e.printStackTrace();
	    }
	    
		return dataset;
	}
}

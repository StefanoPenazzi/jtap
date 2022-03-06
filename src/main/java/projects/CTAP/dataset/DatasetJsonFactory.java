package projects.CTAP.dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.Config;
import controller.Controller;
import core.dataset.ModelElementI;
import core.dataset.ParameterI;

public final class DatasetJsonFactory {
	
	
	public Dataset run(){
		Config config = Controller.getConfig();
		ObjectMapper mapper = new ObjectMapper();
		List<ModelElementI> modelElements = new ArrayList<>();
		
		    String dir = config.getCtapModelConfig().getDatasetConfig().getParamsDirectory();
			try {
				//indexes
				modelElements.add(mapper.readValue(new File(dir+"AgentsIndex.json"), AgentsIndex.class));
				modelElements.add(mapper.readValue(new File(dir+"ActivitiesIndex.json"), ActivitiesIndex.class));
				modelElements.add(mapper.readValue(new File(dir+"CitiesDsIndex.json"), CitiesDsIndex.class));
				modelElements.add(mapper.readValue(new File(dir+"CitiesDsIndex.json"), CitiesOsIndex.class));
				modelElements.add(mapper.readValue(new File(dir+"TimeIndex.json"), TimeIndex.class));
				
				//params
				modelElements.add(mapper.readValue(new File(dir+"AttractivenessParameter.json"), AttractivenessParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"Ds2DsTravelCostParameter.json"), Ds2DsTravelCostParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"Ds2OsTravelCostParameter.json"), Ds2OsTravelCostParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"DurationDiscomfortParameter.json"), DurationDiscomfortParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"MonetaryBudgetParameter.json"), MonetaryBudgetParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"Os2DsTravelCostParameter.json"), Os2DsTravelCostParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"PercOfTimeTargetParameter.json"), PercOfTimeTargetParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"TimeDurationParameter.json"), TimeDurationParameter.class));
				modelElements.add(mapper.readValue(new File(dir+"TimeRelatedBudgetParameter.json"), TimeRelatedBudgetParameter.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return new Dataset(modelElements);
	}

}

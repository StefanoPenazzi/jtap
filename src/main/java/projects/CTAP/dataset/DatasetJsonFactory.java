package projects.CTAP.dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.Config;
import controller.Controller;
import core.dataset.ParameterI;

public final class DatasetJsonFactory {
	
	
	public Dataset run(){
		Config config = Controller.getConfig();
		ObjectMapper mapper = new ObjectMapper();
		List<ParameterI> params = new ArrayList<>();
		
			try {
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"AttractivenessParameter.json"), AttractivenessParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"Ds2DsTravelCostParameter.json"), Ds2DsTravelCostParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"Ds2OsTravelCostParameter.json"), Ds2OsTravelCostParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"DurationDiscomfortParameter.json"), DurationDiscomfortParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"MonetaryBudgetParameter.json"), MonetaryBudgetParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"Os2DsTravelCostParameter.json"), Os2DsTravelCostParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"PercOfTimeTargetParameter.json"), PercOfTimeTargetParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"TimeDurationParameter.json"), TimeDurationParameter.class));
				params.add(mapper.readValue(new File(config.getCtapModelConfig().getDatasetConfig().getParamsDirectory()+"TimeRelatedBudgetParameter.json"), TimeRelatedBudgetParameter.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return new Dataset(params);
	}

}

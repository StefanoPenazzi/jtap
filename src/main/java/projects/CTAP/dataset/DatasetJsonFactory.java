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
		Dataset dataset = null;
		    String dir = config.getCtapModelConfig().getDatasetConfig().getParamsDirectory();
			try {
				//indexes
				AgentsIndex agentsIndex = mapper.readValue(new File(dir+"AgentsIndex.json"), AgentsIndex.class);
				ActivitiesIndex activitiesIndex = mapper.readValue(new File(dir+"ActivitiesIndex.json"), ActivitiesIndex.class);
				CitiesDsIndex citiesDsIndex = mapper.readValue(new File(dir+"CitiesDsIndex.json"), CitiesDsIndex.class);
				CitiesOsIndex citiesOsIndex = mapper.readValue(new File(dir+"CitiesOsIndex.json"), CitiesOsIndex.class);
				TimeIndex timeIndex = mapper.readValue(new File(dir+"TimeIndex.json"), TimeIndex.class);
				
				//params
				AttractivenessParameter attractivenessParameter = mapper.readValue(new File(dir+"AttractivenessParameter.json"), AttractivenessParameter.class);
				Ds2DsTravelCostParameter ds2DsTravelCostParameter = mapper.readValue(new File(dir+"Ds2DsTravelCostParameter.json"), Ds2DsTravelCostParameter.class);
				Ds2OsTravelCostParameter ds2OsTravelCostParameter =  mapper.readValue(new File(dir+"Ds2OsTravelCostParameter.json"), Ds2OsTravelCostParameter.class);
				DurationDiscomfortParameter durationDiscomfortParameter =  mapper.readValue(new File(dir+"DurationDiscomfortParameter.json"), DurationDiscomfortParameter.class);
				MonetaryBudgetParameter monetaryBudgetParameter = mapper.readValue(new File(dir+"MonetaryBudgetParameter.json"), MonetaryBudgetParameter.class);
				Os2DsTravelCostParameter os2DsTravelCostParameter = mapper.readValue(new File(dir+"Os2DsTravelCostParameter.json"), Os2DsTravelCostParameter.class);
				PercOfTimeTargetParameter percOfTimeTargetParameter = mapper.readValue(new File(dir+"PercOfTimeTargetParameter.json"), PercOfTimeTargetParameter.class);
				TimeDurationParameter timeDurationParameter = mapper.readValue(new File(dir+"TimeDurationParameter.json"), TimeDurationParameter.class);
				TimeRelatedBudgetParameter timeRelatedBudgetParameter = mapper.readValue(new File(dir+"TimeRelatedBudgetParameter.json"), TimeRelatedBudgetParameter.class);
				//LocationPerceptionParameter locationPerceptionParameter = mapper.readValue(new File(dir+"LocationPerceptionParameter.json"), LocationPerceptionParameter.class);
				ActivityLocationCostParameter activityLocationCostParameter = mapper.readValue(new File(dir+"ActivityLocationCostParameter.json"), ActivityLocationCostParameter.class);
				ValueOfTimeParameter valueOfTimeParameter = mapper.readValue(new File(dir+"ValueOfTimeParameter.json"), ValueOfTimeParameter.class);
				AgentHomeLocationParameter agentHomeLocationParameter = mapper.readValue(new File(dir+"AgentHomeLocationParameter.json"), AgentHomeLocationParameter.class);
			
		
			dataset = new Dataset( agentsIndex,
					  activitiesIndex,
					  citiesDsIndex,
					  citiesOsIndex,
					 timeIndex,
					 attractivenessParameter,
					 ds2DsTravelCostParameter,
					 ds2OsTravelCostParameter,
					 durationDiscomfortParameter,
					 monetaryBudgetParameter,
					 os2DsTravelCostParameter,
					 percOfTimeTargetParameter,
					 timeDurationParameter,
					 timeRelatedBudgetParameter,
					 //locationPerceptionParameter,
					 activityLocationCostParameter,
					 valueOfTimeParameter,
					 agentHomeLocationParameter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return dataset;
	}

}

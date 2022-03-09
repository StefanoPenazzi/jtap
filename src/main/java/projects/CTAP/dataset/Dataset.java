package projects.CTAP.dataset;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Provider;

import core.dataset.DatasetI;
import core.dataset.ModelElementI;
import core.dataset.ParameterI;

public final class Dataset implements DatasetI {
	
	private final AgentsIndex agentsIndex ;
	private final ActivitiesIndex activitiesIndex ;
	private final CitiesDsIndex citiesDsIndex ;
	private final CitiesOsIndex citiesOsIndex ;
	private final TimeIndex timeIndex ;
	
	//params
	private final AttractivenessParameter attractivenessParameter ;
	private final Ds2DsTravelCostParameter ds2DsTravelCostParameter ;
	private final Ds2OsTravelCostParameter ds2OsTravelCostParameter ;
	private final DurationDiscomfortParameter durationDiscomfortParameter ;
	private final MonetaryBudgetParameter monetaryBudgetParameter ;
	private final Os2DsTravelCostParameter os2DsTravelCostParameter ;
	private final PercOfTimeTargetParameter percOfTimeTargetParameter ;
	private final TimeDurationParameter timeDurationParameter ;
	private final TimeRelatedBudgetParameter timeRelatedBudgetParameter ;
	//private final LocationPerceptionParameter locationPerceptionParameter ;
	private final ActivityLocationCostParameter activityLocationCostParameter ;
	private final ValueOfTimeParameter valueOfTimeParameter;
	private final AgentHomeLocationParameter agentHomeLocationParameter;
	
	
	
	public Dataset(AgentsIndex agentsIndex,
	 ActivitiesIndex activitiesIndex,
	 CitiesDsIndex citiesDsIndex,
	 CitiesOsIndex citiesOsIndex,
	 TimeIndex timeIndex,
	 AttractivenessParameter attractivenessParameter,
	 Ds2DsTravelCostParameter ds2DsTravelCostParameter,
	 Ds2OsTravelCostParameter ds2OsTravelCostParameter,
	 DurationDiscomfortParameter durationDiscomfortParameter,
	 MonetaryBudgetParameter monetaryBudgetParameter,
	 Os2DsTravelCostParameter os2DsTravelCostParameter,
	 PercOfTimeTargetParameter percOfTimeTargetParameter,
	 TimeDurationParameter timeDurationParameter,
	 TimeRelatedBudgetParameter timeRelatedBudgetParameter,
	 //LocationPerceptionParameter  locationPerceptionParameter,
	 ActivityLocationCostParameter activityLocationCostParameter,
	 ValueOfTimeParameter valueOfTimeParameter,
	 AgentHomeLocationParameter agentHomeLocationParameter) {
		
		this.agentsIndex = agentsIndex ;
		this.activitiesIndex = activitiesIndex;
		this.citiesDsIndex = citiesDsIndex;
		this.citiesOsIndex = citiesOsIndex;
		this.timeIndex = timeIndex;
		this.attractivenessParameter = attractivenessParameter;
		this.ds2DsTravelCostParameter = ds2DsTravelCostParameter;
		this.ds2OsTravelCostParameter = ds2OsTravelCostParameter;
		this.durationDiscomfortParameter = durationDiscomfortParameter;
		this.monetaryBudgetParameter = monetaryBudgetParameter;
		this.os2DsTravelCostParameter = os2DsTravelCostParameter;
		this.percOfTimeTargetParameter = percOfTimeTargetParameter;
		this.timeDurationParameter = timeDurationParameter;
		this.timeRelatedBudgetParameter = timeRelatedBudgetParameter;
		//this.locationPerceptionParameter = locationPerceptionParameter;
		this.activityLocationCostParameter = activityLocationCostParameter;
		this.valueOfTimeParameter = valueOfTimeParameter;
		this.agentHomeLocationParameter = agentHomeLocationParameter;
		
	}
	public ActivitiesIndex getActivitiesIndex() {
		return activitiesIndex;
	}
	public AgentsIndex getAgentsIndex() {
		return agentsIndex;
	}
	public TimeIndex getTimeIndex() {
		return timeIndex;
	}
	public CitiesDsIndex getCitiesDsIndex() {
		return citiesDsIndex;
	}
	public CitiesOsIndex getCitiesOsIndex() {
		return citiesOsIndex;
	}
	public AttractivenessParameter getAttractivenessParameter() {
		return attractivenessParameter;
	}
	public Ds2DsTravelCostParameter getDs2DsTravelCostParameter() {
		return ds2DsTravelCostParameter;
	}
	public Ds2OsTravelCostParameter getDs2OsTravelCostParameter() {
		return ds2OsTravelCostParameter;
	}
	public DurationDiscomfortParameter getDurationDiscomfortParameter() {
		return durationDiscomfortParameter;
	}
	public MonetaryBudgetParameter getMonetaryBudgetParameter() {
		return monetaryBudgetParameter;
	}
	public Os2DsTravelCostParameter getOs2DsTravelCostParameter() {
		return os2DsTravelCostParameter;
	}
	public PercOfTimeTargetParameter getPercOfTimeTargetParameter() {
		return percOfTimeTargetParameter;
	}
	public TimeDurationParameter getTimeDurationParameter() {
		return timeDurationParameter;
	}
	public TimeRelatedBudgetParameter getTimeRelatedBudgetParameter() {
		return timeRelatedBudgetParameter;
	}
	//public LocationPerceptionParameter getLocationPerceptionParameter() {
	//	return locationPerceptionParameter;
	//}
	public ActivityLocationCostParameter getActivityLocationCostParameter() {
		return activityLocationCostParameter;
	}
	public ValueOfTimeParameter getValueOfTimeParameter() {
		return valueOfTimeParameter;
	}
	public AgentHomeLocationParameter getAgentHomeLocationParameter() {
		return agentHomeLocationParameter;
	}
}

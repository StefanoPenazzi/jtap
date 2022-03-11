package projects.CTAP.population;

import java.util.List;

import core.models.ModelI;
import core.population.AgentI;

public class Agent implements AgentI {
	
	private final List<ModelI> agentModels;
	private final Long agentId;
	private final Long locationId;
	private List<Plan> optimalPlans;
	
	public Agent(Long agentId,Long locationId, List<ModelI> agentModels) {
		this.agentModels = agentModels;
		this.agentId = agentId;
		this.locationId = locationId;
	}
	
	public List<ModelI> getAgentModels() {
		return agentModels;
	}
	
	public List<Plan> getOptimalPlans() {
		return optimalPlans;
	}
	
	public void setOptimalPlans(List<Plan> optimalPlans) {
		this.optimalPlans = optimalPlans;
	}
	public Long getAgentId() {
		return agentId;
	}
	
	public Long getLocationId() {
		return locationId;
	}

}

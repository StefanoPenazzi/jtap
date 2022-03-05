package projects.CTAP.model;

import java.util.List;

import core.models.ModelI;
import projects.CTAP.dataset.Dataset;

public class AgentPlansFactory {
	
	private final int nPlanActivities;
	private final Dataset dataset;
	
	
	public AgentPlansFactory (int nPlanActivities, Dataset dataset) {
		this.nPlanActivities = 0;
		this.dataset = dataset;
		
	}
	
	public List<ModelI> run(){
		return null;
	}

}

package projects.CTAP.model;

import java.util.ArrayList;
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
	
	private Long[][] getCombinations(){
		List<Long[]> res = new ArrayList<>();
		int maxCombinations = 100;
		for(int i =0;i<maxCombinations;i++) {
			Long[] ll = new Long[this.nPlanActivities];
			for(int j = 0;j<this.nPlanActivities;j+=2) {
				ll[j] = 0L;
			}
		}
		return null;
	}

}

package projects.CTAP.model;

import java.util.ArrayList;
import java.util.List;
import core.models.ModelI;
import projects.CTAP.dataset.Dataset;

public class AgentFactory {
	
	private final int nPlanActivities;
	private final Dataset dataset;
	private final Long agentId;
	private final Long homeLocationId;
	private final int agentIndex;
	private final int homeLocationIndex;
	private final boolean homeOs;
	
	
	public AgentFactory (Long agentId,Long homeLocationId,int nPlanActivities ,Dataset dataset) {
		this.nPlanActivities = 0;
		this.dataset = dataset;
		this.agentId = agentId;
		this.homeLocationId = homeLocationId;
		this.agentIndex = dataset.getAgentsIndex().getIndex().indexOf(agentId);
		this.homeLocationIndex = dataset.getCitiesOsIndex().getIndex().indexOf(homeLocationId);
		this.homeOs = true;
	}
	
	public Agent run(){
		
		List<int[][]> activityLocationSeq = getCombinations();
		List<ModelI> models = new ArrayList<>();
		
		//cost
		double monetaryBudget = this.dataset.getMonetaryBudgetParameter().getParameter()[agentIndex];
		double timeRelatedBudget = this.dataset.getMonetaryBudgetParameter().getParameter()[agentIndex];
		double valueOfTime = this.dataset.getValueOfTimeParameter().getParameter()[agentIndex];;
		
		for(int[][] al: activityLocationSeq) {
			
			//activities seq
			double[] percOfTimeTargetParameter =  new double[nPlanActivities];
			double[] timeDuration =  new double[nPlanActivities];
			
			//locations seq
			double[] locationPerception = new double[nPlanActivities];
			double[] sigmaActivityCalibration = new double[nPlanActivities];
			double[] tauActivityCalibration = new double[nPlanActivities];
			double[] gammaActivityCalibration = new double[nPlanActivities];
			double[] travelCost = new double[nPlanActivities];
			double[] travelTime = new double[nPlanActivities];
			double[] activityLocationCostRate = new double[nPlanActivities];
			float[][] attractiveness = new float[nPlanActivities][dataset.getTimeIndex().getIndex().size()];
			
			//factory
            for(int i = 0;i< this.nPlanActivities ;i++) {
            	
            	percOfTimeTargetParameter[i] = this.dataset.getPercOfTimeTargetParameter().getParameter()[agentIndex][al[0][i]];
				timeDuration[i] = this.dataset.getTimeDurationParameter().getParameter()[agentIndex][al[0][i]];
            	
            	//locationPerception[i] = this.dataset.getLocationPerceptionParameter().getParameter()[al[1][i]];
    			sigmaActivityCalibration[i] = 0;
    			tauActivityCalibration[i] = 0;
    			//assuming first activity is always the default one
    			if(i%2==0 && i < this.nPlanActivities-1) {
    				travelCost[i] = this.dataset.getOs2DsTravelCostParameter().getParameter()[0][al[1][i]][al[1][i+1]];   //TODO check if this is not DsDs, and which project to use
        			travelTime[i] = 0;
    			}
    			else if(i%2!=0 && i < this.nPlanActivities-1) {
    				travelCost[i] = this.dataset.getDs2DsTravelCostParameter().getParameter()[0][al[1][i]][al[1][i+1]];   //TODO check if this is not DsDs, and which project to use
        			travelTime[i] = 0;
    			}
    			
    			activityLocationCostRate[i] = this.dataset.getActivityLocationCostParameter().getParameter()[al[0][i]][al[1][i]];
    			attractiveness[i] = this.dataset.getAttractivenessParameter().getParameter()[agentIndex][al[0][i]][al[1][i]];
			}
			
			ObjectiveFunctionCTAP_01 objF = new ObjectiveFunctionCTAP_01(nPlanActivities,
					al[0], al[1], percOfTimeTargetParameter, timeDuration, locationPerception,
					sigmaActivityCalibration,tauActivityCalibration,gammaActivityCalibration,
					travelCost,travelTime,monetaryBudget, timeRelatedBudget, activityLocationCostRate,
					valueOfTime,attractiveness);
			
			models.add(new ModelCTAP(objF,null));
			
		}
		
		return new Agent(this.agentId,this.homeLocationId,models);
	}
	
	//TODO
	//activity sequence 01010101...
	//location sequence 01010101...
	private List<int[][]> getCombinations(){
		
		int maxCombinations = 100;
		List<int[]> activitiesPlan = new ArrayList<>();
		//they should be different now they are all the same
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[this.nPlanActivities];
			for(int j = 0;j<this.nPlanActivities;j+=2) {
				ll[j] = 0;
			}
			for(int j = 1;j<this.nPlanActivities;j+=2) {
				ll[j] = 1;
			}
			activitiesPlan.add(ll);
		}
		
		List<int[]> locationsPlan = new ArrayList<>();
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[this.nPlanActivities];
			for(int j = 0;j<this.nPlanActivities;j+=2) {
				ll[j] = 0;  //home location
			}
			for(int j = 1;j<this.nPlanActivities;j+=2) {
				ll[j] = 1;  //other location DS
			}
			locationsPlan.add(ll);
		}
		
		List<int[][]> res = new ArrayList<>();
		for(int i =0;i<maxCombinations;i++) {
			int[][] singlePlan = new int[2][this.nPlanActivities];
			for(int j =0;j<this.nPlanActivities;j++) {
				singlePlan[0] = activitiesPlan.get(i);
				singlePlan[1] = locationsPlan.get(i);
			}
			res.add(singlePlan);
		}
		
		return res;
	}

}

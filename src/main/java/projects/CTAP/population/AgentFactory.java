package projects.CTAP.population;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.models.ModelI;
import core.population.AgentFactoryI;
import projects.CTAP.dataset.Dataset;
import projects.CTAP.model.ModelCTAP;
import projects.CTAP.model.ObjectiveFunctionCTAP_01;

public class AgentFactory implements AgentFactoryI {
	
	private final int nPlanActivities;
	private final Dataset dataset;
	private final Long agentId;
	private final Long homeLocationId;
	private final int agentIndex;
	private final int homeLocationIndex;
	private final boolean homeOs;
	
	
	public AgentFactory (Long agentId,Long homeLocationId,int nPlanActivities ,Dataset dataset) {
		this.nPlanActivities = nPlanActivities;
		this.dataset = dataset;
		this.agentId = agentId;
		this.homeLocationId = homeLocationId;
		this.agentIndex = dataset.getAgentsIndex().getIndex().indexOf(agentId);
		this.homeLocationIndex = dataset.getCitiesOsIndex().getIndex().indexOf(homeLocationId);
		this.homeOs = true;
	}
	
	public Agent run(){
		
		boolean homeDs = dataset.getCitiesDsIndex().getIndex().contains(this.homeLocationId); 
		List<int[][]> activityLocationSeq = getRandomCombinations();
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
            	
            	int actIndex = al[0][i];
            	int locIndex = al[1][i];
            	
            	percOfTimeTargetParameter[i] = this.dataset.getPercOfTimeTargetParameter().getParameter()[agentIndex][actIndex ];
				timeDuration[i] = this.dataset.getTimeDurationParameter().getParameter()[agentIndex][actIndex];
            	
            	//locationPerception[i] = this.dataset.getLocationPerceptionParameter().getParameter()[al[1][i]];
    			sigmaActivityCalibration[i] = 1;
    			tauActivityCalibration[i] = 1;
    			//assuming first activity is always the default one
    			if(i%2==0 && i < this.nPlanActivities-1) {
    				int nextLocIndex = al[1][i+1];
    				if(homeDs) {
    					travelCost[i] = this.dataset.getDs2DsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
            			travelTime[i] = 0;
    				}
    				else {
    					travelCost[i] = this.dataset.getOs2DsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
            			travelTime[i] = 0;
    				}
    			}
    			else if(i%2!=0 && i < this.nPlanActivities-1) {
    				int nextLocIndex = al[1][i+1];
    				if(homeDs) {
	    				travelCost[i] = this.dataset.getDs2DsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
	        			travelTime[i] = 0;
    				}
    				else {
    					travelCost[i] = this.dataset.getDs2OsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
	        			travelTime[i] = 0;
    				}
    				activityLocationCostRate[i] = this.dataset.getActivityLocationCostParameter().getParameter()[locIndex][actIndex];
        			attractiveness[i] = this.dataset.getAttractivenessParameter().getParameter()[agentIndex][locIndex][actIndex];
    			}
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
	//random 
	private List<int[][]> getRandomCombinations(){
		
		int maxCombinations = 100;
		long totComb = (long) (dataset.getActivitiesIndex().getIndex().size() * dataset.getCitiesDsIndex().getIndex().size());
		Random rand = new Random(); 
		boolean homeDs = dataset.getCitiesDsIndex().getIndex().contains(this.homeLocationId);    //if the home is in the DS cities
		
		List<int[]> activitiesPlan = new ArrayList<>();
		//they should be different now they are all the same
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[this.nPlanActivities];
			for(int j = 0;j<this.nPlanActivities;j+=2) {
				ll[j] = 0;  //default activity
			}
			for(int j = 1;j<this.nPlanActivities;j+=2) {
				ll[j] = rand.nextInt(dataset.getActivitiesIndex().getIndex().size()-1)+1;
			}
			activitiesPlan.add(ll);
		}
		
		List<int[]> locationsPlan = new ArrayList<>();
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[this.nPlanActivities];
			for(int j = 0;j<this.nPlanActivities;j+=2) {
				ll[j] = homeLocationIndex;  //home location
			}
			for(int j = 1;j<this.nPlanActivities;j+=2) {
				int citiesDsSize = dataset.getCitiesDsIndex().getIndex().size();
				
				if(!homeDs) {
					ll[j] = rand.nextInt(citiesDsSize);
				}
				else {
					//I don't want to have the home location
					int z = homeLocationIndex;
					while(z == homeLocationIndex) {
						z = rand.nextInt(citiesDsSize);
					}
					ll[j] = z;			
				}
				
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

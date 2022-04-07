package projects.CTAP.population;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.inject.Inject;

import config.Config;
import core.models.ConstraintI;
import core.models.ModelI;
import core.population.AgentFactoryI;
import projects.CTAP.dataset.Dataset;
import projects.CTAP.model.ActivityLocationI;
import projects.CTAP.model.LowerBoundCTAP;
import projects.CTAP.model.ModelCTAP;
import projects.CTAP.model.ObjectiveFunctionCTAP_01;
import projects.CTAP.model.UpperBoundCTAP;

public class AgentFactory implements AgentFactoryI {
	
	private Config config;
	private ActivityLocationI activityLocation;
	
	@Inject
	public AgentFactory (Config config, ActivityLocationI activityLocation) {
		this.config = config;
		this.activityLocation = activityLocation;
	}
	
	@Override
	public Agent run(Long agentId,Long homeLocationId,Dataset dataset){
		
		int agentIndex = dataset.getAgentsIndex().getIndex().indexOf(agentId);
		int nPlanActivities = this.config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getPlanSize();
		boolean homeDs = dataset.getCitiesDsIndex().getIndex().contains(homeLocationId); 
		List<ModelI> models = new ArrayList<>();
		
		//cost
		double monetaryBudget = dataset.getMonetaryBudgetParameter().getParameter()[agentIndex];
		double timeRelatedBudget = dataset.getMonetaryBudgetParameter().getParameter()[agentIndex];
		double valueOfTime = dataset.getValueOfTimeParameter().getParameter()[agentIndex];;
		
		for(int[][] al: this.activityLocation.run(agentId, homeLocationId, dataset)) {
			
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
            for(int i = 0;i< nPlanActivities ;i++) {
            	
            	int actIndex = al[0][i];
            	int locIndex = al[1][i];
            	
            	percOfTimeTargetParameter[i] = dataset.getPercOfTimeTargetParameter().getParameter()[agentIndex][actIndex ];
				timeDuration[i] = dataset.getTimeDurationParameter().getParameter()[agentIndex][actIndex];
            	
            	//locationPerception[i] = this.dataset.getLocationPerceptionParameter().getParameter()[al[1][i]];
    			sigmaActivityCalibration[i] = 1;
    			tauActivityCalibration[i] = 1;
    			//assuming first activity is always the default one
    			if(i%2==0 && i < nPlanActivities-1) {
    				int nextLocIndex = al[1][i+1];
    				if(homeDs) {
    					travelCost[i] = dataset.getDs2DsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
            			travelTime[i] = 0;
    				}
    				else {
    					travelCost[i] = dataset.getOs2DsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
            			travelTime[i] = 0;
    				}
    			}
    			else if(i%2!=0 && i < nPlanActivities-1) {
    				int nextLocIndex = al[1][i+1];
    				if(homeDs) {
	    				travelCost[i] = dataset.getDs2DsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
	        			travelTime[i] = 0;
    				}
    				else {
    					travelCost[i] = dataset.getDs2OsTravelCostParameter().getParameter()[0][locIndex][nextLocIndex];   //TODO check if this is not DsDs, and which project to use
	        			travelTime[i] = 0;
    				}
    				activityLocationCostRate[i] = dataset.getActivityLocationCostParameter().getParameter()[locIndex][actIndex];
        			attractiveness[i] = dataset.getAttractivenessParameter().getParameter()[agentIndex][locIndex][actIndex];
    			}
			}
			
			ObjectiveFunctionCTAP_01 objF = new ObjectiveFunctionCTAP_01(nPlanActivities,
					al[0], al[1], percOfTimeTargetParameter, timeDuration, locationPerception,
					sigmaActivityCalibration,tauActivityCalibration,gammaActivityCalibration,
					travelCost,travelTime,monetaryBudget, timeRelatedBudget, activityLocationCostRate,
					valueOfTime,attractiveness);
			List<ConstraintI> constraints = new ArrayList<>();
			double[] lb = new double[nPlanActivities*2];
			double[] ub = new double[nPlanActivities*2];
			Arrays.fill(lb, 0d);
			Arrays.fill(ub, 8760d);
			constraints.add(new LowerBoundCTAP(lb));
			constraints.add(new UpperBoundCTAP(ub));
			
			models.add(new ModelCTAP(objF,constraints));
			
		}
		
		return new Agent(agentId,homeLocationId,models);
	}
	
}

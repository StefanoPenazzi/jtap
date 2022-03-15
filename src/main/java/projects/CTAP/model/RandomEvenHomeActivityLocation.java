package projects.CTAP.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.inject.Inject;

import config.Config;
import projects.CTAP.dataset.Dataset;

public class RandomEvenHomeActivityLocation implements ActivityLocationI {

	private Config config;
	
	@Inject
	public RandomEvenHomeActivityLocation(Config config) {
		this.config = config;
	}
	
	@Override
	public List<int[][]> run(Long agentId, Long homeLocationId, Dataset dataset) {
		
		int maxCombinations = config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getMaxPlans();
		int nPlanActivities = config.getCtapModelConfig().getCtapPopulationConfig().getCtapAgentConfig().getPlanSize();
		int homeLocationIndex = dataset.getCitiesOsIndex().getIndex().indexOf(homeLocationId);
		
		Random rand = new Random(); 
		boolean homeDs = dataset.getCitiesDsIndex().getIndex().contains(homeLocationId);    //if the home is in the DS cities
		
		List<int[]> activitiesPlan = new ArrayList<>();
		//they should be different now they are all the same
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[nPlanActivities];
			for(int j = 0;j<nPlanActivities;j+=2) {
				ll[j] = 0;  //default activity
			}
			for(int j = 1;j<nPlanActivities;j+=2) {
				ll[j] = rand.nextInt(dataset.getActivitiesIndex().getIndex().size()-1)+1;
			}
			activitiesPlan.add(ll);
		}
		
		List<int[]> locationsPlan = new ArrayList<>();
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[nPlanActivities];
			for(int j = 0;j<nPlanActivities;j+=2) {
				ll[j] = homeLocationIndex;  //home location
			}
			for(int j = 1;j<nPlanActivities;j+=2) {
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
			int[][] singlePlan = new int[2][nPlanActivities];
			for(int j =0;j<nPlanActivities;j++) {
				singlePlan[0] = activitiesPlan.get(i);
				singlePlan[1] = locationsPlan.get(i);
			}
			res.add(singlePlan);
		}
		
		return res;
		
	}

}

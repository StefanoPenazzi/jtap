package projects.CTAP.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.inject.Inject;

import config.Config;
import projects.CTAP.dataset.Dataset;

public class ProbDistEvenHomeActivityLocation implements ActivityLocationI {

	private Config config;
	
	@Inject
	public ProbDistEvenHomeActivityLocation(Config config) {
		this.config = config;
	}
	
	@Override
	public List<int[][]> run(Long agentId, Long homeLocationId, Dataset dataset) {
		
		int maxCombinations = config.getCtapModelConfig().getCtapPopulationConfig()
				.getCtapAgentConfig().getMaxPlans();
		int nPlanActivities = config.getCtapModelConfig().getCtapPopulationConfig()
				.getCtapAgentConfig().getPlanSize();
		boolean homeDs = dataset.getCitiesDsIndex().getIndex().contains(homeLocationId); 
		int homeLocationIndex = homeDs ? dataset.getCitiesDsIndex().getIndex()
				.indexOf(homeLocationId) : dataset.getCitiesOsIndex().getIndex()
				.indexOf(homeLocationId);
		
		Random rand = new Random(); 
		
		//ACTIVITIES------------------------------------------------------------
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
		
		//LOCATIONS-------------------------------------------------------------
		List<int[]> locationsPlan = new ArrayList<>();
		//CDF 
		double[] destProb = dataset.getDestinationsProbDistParameter()
				.getParameter()[dataset.getDestinationsProbDistParameter().
				                getParameterDescription().get(0).indexOf(homeLocationId)];
		double[] destProbCDF = new double[destProb.length];
		destProbCDF[0]=destProb[0];
		for(int i =1;i<destProb.length;i++) {
			destProbCDF[i]=destProbCDF[i-1]+destProb[i];
		}
		
		for(int i =0;i<maxCombinations;i++) {
			int[] ll = new int[nPlanActivities];
			for(int j = 0;j<nPlanActivities;j+=2) {
				ll[j] = homeLocationIndex;  //home location
			}
			
			for(int j = 1;j<nPlanActivities;j+=2) {
				double rr = rand.nextDouble();
				//binarySearchForIndex doesn't manage values smaller than destProbCDF[0]
				if(rr <= destProbCDF[0] ) {
					ll[j] = 0;
				}
				else {
					ll[j] = binarySearchForIndex(destProbCDF,0,destProbCDF.length-1,rr);
				}
			}
			locationsPlan.add(ll);
		}
		
		//RESULT----------------------------------------------------------------
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
	
	//it finds the upper value and return the index
	//arr must be a sorted array (ascend)
	private int binarySearchForIndex(double arr[], int l, int r, double x)
    {
        if (r >= l) {
            int mid = l + (r - l) / 2;
 
            if (arr[mid] == x)
                return mid;
            //left
            if (arr[mid] > x) {
            	if (arr[mid-1]<x) return mid;
                return binarySearchForIndex(arr, l, mid - 1, x);
            }
            //right
            else {
            	if (arr[mid+1]>x) return mid;
            	return binarySearchForIndex(arr, mid + 1, r, x);
            }
        }
        return -1;
    }

}
package projects.CTAP.model;

import java.util.Arrays;

import core.models.ObjectiveFunctionI;

public class ObjectiveFunctionCTAP implements ObjectiveFunctionI {
	
	
	private final int nActivities;
	private final int[] activities;
	private final int[] locations;
	private final double[] percentageOfTimeTarget;
	private final double[] timeDuration;
	private final double[] locationPerception;
	private final double[] sigmaActivityCalibration;
	private final double[] tauActivityCalibration;
	private final double[] durationDiscomfort;
	private final double[] travelCost;
	private final double[] travelTime;
	private final float[][] attractiveness;
	
	private final double monetaryBudget;
	private final double timeRelatedBudget;
	private final double valueOfTime;
	
	
	
	public ObjectiveFunctionCTAP(int nActivities,
			                     int[] activities,
								 int[] locations,
								 double[] percentageOfTimeTarget,
								 double[] timeDuration,
								 double[] locationPerception,
								 double[] sigmaActivityCalibration,
								 double[] tauActivityCalibration,
								 double[] durationDiscomfort,
								 double[] travelCost,
								 double[] travelTime,
								 float[][] attractiveness,
								 double valueOfTime,
								 double monetaryBudget,
								 double timeRelatedBudget
								 ) {
		
		 this.nActivities = nActivities;
		 this.activities = activities;
		 this.locations = locations;
		 this.percentageOfTimeTarget = percentageOfTimeTarget;
		 this.timeDuration = timeDuration;
		 this.locationPerception = locationPerception;
		 this.sigmaActivityCalibration = sigmaActivityCalibration;
		 this.tauActivityCalibration = tauActivityCalibration;
		 this.durationDiscomfort = durationDiscomfort;
		 this.travelCost = travelCost;
		 this.travelTime = travelTime;
		 this.monetaryBudget = monetaryBudget;
		 this.timeRelatedBudget = timeRelatedBudget;
		 this.attractiveness = attractiveness ;
		 this.valueOfTime = valueOfTime;
	}
	
	public double getValue(double[] ts, double[] te) {
		double res = 0;
		res += getDiscomfortPercentageOfTimeTarget(ts, te);
		//res += getDiscomfortDurationTarget(ts, te);
		//res += getDiscomfortBudget(ts, te);
		res += getLagrangeMultipliers(ts, te);
		return res;
	}
	
	@Override
	public double getValue(double[] t) {
		double[] ts = Arrays.copyOfRange(t,0,t.length/2);
		double[] te = Arrays.copyOfRange(t,t.length/2,t.length);
		return getValue(ts,te); 
		
	}
	
	
	private double getDiscomfortPercentageOfTimeTarget(double[] ts, double[] te) {
		double res = 0;
		for(int i = 0;i<nActivities;i++) {
			res += Math.pow(percentageOfTimeTarget[i] - getStateValue(i,ts,te),2);
		}
		return res;
	}
	
	private double getDiscomfortDurationTarget(double[] ts, double[] te) {
		double res = 0;
		for(int i = 0;i<activities.length;i++) {
			res += Math.pow(timeDuration[activities[i]] - (te[i]-ts[i]),2);
			res = res*this.durationDiscomfort[i];
		}
		return res;
	}
	
	private double getDiscomfortBudget(double[] ts, double[] te) {
		double res = 0;
		for(int i =0;i<this.activities.length;i++ ) {
			res += costActivityLocation(i,ts[i],te[i]);
			res += travelCost[i];
		}
		res = Math.pow(res, 2)/Math.pow(monetaryBudget,2);
		res += Math.pow(costOfTime(ts[0],te[te.length-1]), 2)/Math.pow(timeRelatedBudget, 2);
		return res;
	}
	
	private double getPullFactor(int i,double[] ts, double[] te) {
		//TODO insert attractiveness function
		return 100;//locationPerception[i]*1;                               
	}
	
	public double costActivityLocation(int i,double ts, double te) {
		return 0;
	}
	
	public double costOfTime(double ts, double te) {
		return 0;
	}
	
	public double attractiveness(int i,double t) {
		return 1;
	}
	
	private double getStateValue(int activity,double[] ts, double[] te) {
		double res = 0;
		for(int i =0;i<this.activities.length;i++ ) {
			if(activities[i] == activity) {
				res = 1 + (res-1) * Math.pow(Math.E, -tauActivityCalibration[i]*(te[i]-ts[i])* getPullFactor(i,ts, te)); 
			}
			else {
				res = res * Math.pow(Math.E, -sigmaActivityCalibration[i]*(te[i]-ts[i])); 
				
			}
		}
		return res;
	}
	
	public double getLagrangeMultipliers(double[] ts, double[] te) {
		for(int i =0;i<ts.length;i++) {
			double diff = te[i] - ts[i];
			if(diff<=0) {
				return Double.MAX_VALUE;
			}
		}
		for(int i =0;i<ts.length-1;i++) {
			double diff = ts[i+1] - te[i];
			if(diff<=0) {
				return Double.MAX_VALUE;
			}
		}
		return 0;
	}

	@Override
	public int getVariablesLength() {
		return this.nActivities*2;
	}
	
	public int[] getActivities() {
		return activities;
	}
	
	public int[] getLocations() {
		return locations;
	}
	
}

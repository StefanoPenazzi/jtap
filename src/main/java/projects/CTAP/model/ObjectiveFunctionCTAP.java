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
	
	private final double attractivenessTimeInterval;
	
	private final double monetaryBudget;
	private final double timeRelatedBudget;
	private final double valueOfTime;
	
	//TODO to check if more than one routed path is used, check if "origin and destination" use only one projection? 
	// Need to have a for loop amoung all projections
	
	
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
								 double timeRelatedBudget,
								 double attractivenessTimeInterval
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
		 this.attractivenessTimeInterval = attractivenessTimeInterval;
	}
	
	public double getValue(double[] ts, double[] te) {
		double res = 0;
		//res += getDiscomfortPercentageOfTimeTarget(ts, te);
		res += getDiscomfortDurationTarget(ts, te);//TODO Check each method to make sure it is correct, wasn't quite finished
		//res += getDiscomfortBudget(ts, te);
		res += getLagrangeMultipliers_1(ts, te);
		res += getLagrangeMultipliers_2(ts, te);
		//res += getLagrangeMultipliers_3(ts, te);
		//res += getLagrangeMultipliers_4(ts, te);
		
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
		for(int i = 0;i<percentageOfTimeTarget.length;i++) {
			res += Math.pow(percentageOfTimeTarget[i] - getStateValue(i,ts,te),2);
		}
		return res;
	}
	
	private double getDiscomfortDurationTarget(double[] ts, double[] te) {
		double res = 0;
		for(int i = 0;i<activities.length;i++) {
			res += Math.pow(timeDuration[i] - (te[i]-ts[i]),2);
			res = res*1;//this.durationDiscomfort[i];
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
	
	private double getPullFactor(int i,double ts,double te) {
	    if(te < ts) {
			return Math.abs(te-ts)*1000;
		}
		int ts_ = (int) Math.floor(ts / attractivenessTimeInterval);
		int _te = (int) Math.ceil(ts / attractivenessTimeInterval);
		if(ts_ == _te) {
			return locationPerception[i] *  attractiveness[i][ts_]*(te-ts) ;     
		}
		else {
			double res = 0;
			for(int j = ts_+1;j < _te; j++) {
				res += attractivenessTimeInterval*(attractiveness[i][j]);
			}
			res += attractiveness[i][ts_] * ((ts_+1)*attractivenessTimeInterval-ts);
			res += attractiveness[i][_te] * (te - attractivenessTimeInterval*(_te-1));
			return locationPerception[i] * res  ;     
		}                         
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
				res = 1 + (res-1) * Math.pow(Math.E, -tauActivityCalibration[i]*(te[i]-ts[i])* getPullFactor(i,ts[i], te[i])); 
			}
			else {
				res = res * Math.pow(Math.E, -sigmaActivityCalibration[i]*(te[i]-ts[i])); 
			}
		}
		return res;
	}
	
	public double getLagrangeMultipliers_1(double[] ts, double[] te) {
	   double res = 0;
       for(int i =0;i<ts.length;i++) {
			double diff = te[i] - ts[i];
			if(diff <= 0) {
				res += (1+Math.abs(diff));
			}
		}
		return res;
	}
	
	public double getLagrangeMultipliers_2(double[] ts, double[] te) {
		double res = 0;
		for(int j =0;j<ts.length-1;++j) {
			int k = j+1;
			double diff = ts[k] - te[j];
			if(diff <= 0) {
				res = res + (1 + Math.abs(diff));
			}
		}
		return res;
	}
	
	public double getLagrangeMultipliers_3(double[] ts, double[] te) {
		double res = 0;
		for(int j =0;j<ts.length-1;++j) {
			res += costActivityLocation(activities[j],ts[j],te[j]);
			res += travelCost[j];
		}
		if(res > monetaryBudget) {
			return (res-monetaryBudget)*1000;
		}
		else {
			return 0;
		}
		
	}
	
	public double getLagrangeMultipliers_4(double[] ts, double[] te) {
		double res = 0;
		return res;
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

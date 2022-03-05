package projects.CTAP.model;

import core.models.ObjectiveFunctionI;

public class ObjectiveFunctionCTAP implements ObjectiveFunctionI {
	
	
	private final int nActivities;
	private final int[] activitiesSequence;
	private final int[] locations;
	private final double[] percentageOfTimeTargetActivity;
	private final double[] durationTargetActivity;
	private final double[] locationPerception;
	private final double[] sigmaActivityCalibration;
	private final double[] tauActivityCalibration;
	private final double[] gammaActivityCalibration;
	private final double[] travelCost;
	private final double[] travelTime;
	
	private final double monetaryBudget;
	private final double timeRelatedBudget;
	
	
	
	public ObjectiveFunctionCTAP( int nActivities,
			                     int[] activitiesSequence,
								 int[] locations,
								 double[] percentageOfTimeTargetActivity,
								 double[] durationTargetActivity,
								 double[] locationPerception,
								 double[] sigmaActivityCalibration,
								 double[] tauActivityCalibration,
								 double[] gammaActivityCalibration,
								 double[] travelCost,
								 double[] travelTime,
								 double monetaryBudget,
								 double timeRelatedBudget
								 ) {
		
		 this.nActivities = nActivities;
		 this.activitiesSequence = activitiesSequence;
		 this.locations = locations;
		 this.percentageOfTimeTargetActivity = percentageOfTimeTargetActivity;
		 this.durationTargetActivity = durationTargetActivity;
		 this.locationPerception = locationPerception;
		 this.sigmaActivityCalibration = sigmaActivityCalibration;
		 this.tauActivityCalibration = tauActivityCalibration;
		 this.gammaActivityCalibration = gammaActivityCalibration;
		 this.travelCost = travelCost;
		 this.travelTime = travelTime;
		 this.monetaryBudget = monetaryBudget;
		 this.timeRelatedBudget = timeRelatedBudget;
	}
	
	public double getValue(double[] ts, double[] te) {
		double res = 0;
		res += getDiscomfortPercentageOfTimeTarget(ts, te);
		res += getDiscomfortDurationTarget(ts, te);
		res += getDiscomfortBudget(ts, te);
		return res;
	}
	
	
	private double getDiscomfortPercentageOfTimeTarget(double[] ts, double[] te) {
		double res = 0;
		for(int i = 0;i<nActivities;i++) {
			res += Math.pow(percentageOfTimeTargetActivity[i] - getStateValue(i,ts,te),2);
		}
		return res;
	}
	
	private double getDiscomfortDurationTarget(double[] ts, double[] te) {
		double res = 0;
		for(int i = 0;i<activitiesSequence.length;i++) {
			res += Math.pow(durationTargetActivity[activitiesSequence[i]] - (te[i]-ts[i]),2);
			res = res*this.gammaActivityCalibration[i];
		}
		return res;
	}
	
	private double getDiscomfortBudget(double[] ts, double[] te) {
		double res = 0;
		for(int i =0;i<this.activitiesSequence.length;i++ ) {
			res += costActivityLocation(i,ts[i],te[i]);
			res += travelCost[i];
		}
		res = Math.pow(res, 2)/Math.pow(monetaryBudget,2);
		res += Math.pow(costOfTime(ts[0],te[te.length-1]), 2)/Math.pow(timeRelatedBudget, 2);
		return res;
	}
	
	private double getPullFactor(int i,double[] ts, double[] te) {
		//TODO insert attractiveness function
		return locationPerception[i]*1;                               
	}
	
	public double costActivityLocation(int i,double ts, double te) {
		return 0;
	}
	
	public double costOfTime(double ts, double te) {
		return 0;
	}
	
	public double attractiveness(int i,double t) {
		return 0;
	}
	
	private double getStateValue(int activity,double[] ts, double[] te) {
		double res = 0;
		for(int i =0;i<this.activitiesSequence.length;i++ ) {
			if(activitiesSequence[i] == activity) {
				res = 1 + (res-1) * Math.pow(Math.E, -tauActivityCalibration[i]*(te[i]-ts[i])* getPullFactor(i,ts, te)); 
			}
			else {
				res = res * Math.pow(Math.E, -sigmaActivityCalibration[i]*(te[i]-ts[i])); 
			}
		}
		return 0;
	}
}

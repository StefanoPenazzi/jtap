package projects.CTAP.model;


public class ObjectiveFunctionCTAP_01 extends ObjectiveFunctionCTAP {

	private final double[] activityLocationCostRate;
	private final double valueOfTime;
	
	
	public ObjectiveFunctionCTAP_01(int nActivities,
			int[] activitiesSequence,
			int[] locations,
			double[] percentageOfTimeTarget,
			double[] timeDuration,
			double[] locationPerception,
			double[] sigmaActivityCalibration,
			double[] tauActivityCalibration,
			double[] durationDiscomfort,
			double[] travelCost,
			double[] travelTime,
			double monetaryBudget,
			double timeRelatedBudget,
			double[] activityLocationCostRate, 
			double valueOfTime,
			float[][] attractiveness,
			double attractivenessTimeInterval) {
		super(nActivities, activitiesSequence, locations, percentageOfTimeTarget, timeDuration,
				locationPerception, sigmaActivityCalibration, tauActivityCalibration, durationDiscomfort, travelCost,
				travelTime,attractiveness,valueOfTime,monetaryBudget, timeRelatedBudget,attractivenessTimeInterval);
		this.activityLocationCostRate = activityLocationCostRate;
		this.valueOfTime = valueOfTime;
	}

	@Override
	public double costActivityLocation(int i, double ts, double te) {
		return activityLocationCostRate[i]*(te-ts);
	}

	@Override
	public double costOfTime(double ts, double te) {
		return valueOfTime*(te-ts);
	}

	//TODO
	@Override
	public double attractiveness(int i, double t) {
		return 1;
	}
}

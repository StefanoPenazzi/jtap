package projects.CTAP.model;


public class ObjectiveFunctionCTAP_01 extends ObjectiveFunctionCTAP {

	private final double[] activityLocationCostRate;
	private final double costOfTimeRate;
	
	
	public ObjectiveFunctionCTAP_01(int nActivities,
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
			double timeRelatedBudget,
			double[] activityLocationCostRate,
			double costOfTimeRate) {
		super(nActivities, activitiesSequence, locations, percentageOfTimeTargetActivity, durationTargetActivity,
				locationPerception, sigmaActivityCalibration, tauActivityCalibration, gammaActivityCalibration, travelCost,
				travelTime, monetaryBudget, timeRelatedBudget);
		this.activityLocationCostRate = activityLocationCostRate;
		this.costOfTimeRate = costOfTimeRate;
	}

	@Override
	public double costActivityLocation(int i, double ts, double te) {
		return activityLocationCostRate[i]*(te-ts);
	}

	@Override
	public double costOfTime(double ts, double te) {
		return costOfTimeRate*(te-ts);
	}

	//TODO
	@Override
	public double attractiveness(int i, double t) {
		return 1;
	}

}

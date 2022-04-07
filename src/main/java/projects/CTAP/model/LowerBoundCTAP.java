package projects.CTAP.model;

import core.models.ConstraintBoundI;

public class LowerBoundCTAP implements ConstraintBoundI {

	private double[] lowerBound;
	
	public LowerBoundCTAP(double[] lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	@Override
	public double[] getConstraint() {
		return this.lowerBound;
	}

}

package projects.CTAP.model;

import core.models.ConstraintBoundI;

public class UpperBoundCTAP implements ConstraintBoundI {

	private double[] upperBound;
	
	public UpperBoundCTAP(double[] upperBound) {
		this.upperBound = upperBound;
	}
	
	@Override
	public double[] getConstraint() {
		return this.upperBound;
	}

}

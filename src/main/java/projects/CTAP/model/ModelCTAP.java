package projects.CTAP.model;

import java.util.List;

import core.models.ConstraintI;
import core.models.ModelI;
import core.models.ObjectiveFunctionI;

public class ModelCTAP implements ModelI {

	private final ObjectiveFunctionCTAP objF;
	private final List<ConstraintI> constraints;
	private final double[] initialGuess;
	
	public ModelCTAP(ObjectiveFunctionCTAP objF,List<ConstraintI> constraints,double[] initialGuess) {
		this.objF = objF;
		this.constraints = constraints;
		this.initialGuess = initialGuess;
	}
	
	public ModelCTAP(ObjectiveFunctionCTAP objF,List<ConstraintI> constraints) {
		this.objF = objF;
		this.constraints = constraints;
		this.initialGuess = new double[objF.getVariablesLength()];
	}
	
	public ModelCTAP(ObjectiveFunctionCTAP objF) {
		this.objF = objF;
		this.constraints = null;
		this.initialGuess = new double[objF.getVariablesLength()];
	}
	
	@Override
	public ObjectiveFunctionCTAP getObjectiveFunction() {
		return this.objF;
	}

	@Override
	public List<ConstraintI> getConstraints() {
		return this.constraints;
	}

	@Override
	public double[] getInitialGuess() {
		return this.initialGuess;
	}
}

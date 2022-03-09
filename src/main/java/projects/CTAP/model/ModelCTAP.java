package projects.CTAP.model;

import java.util.List;

import core.models.ConstraintI;
import core.models.ModelI;
import core.models.ObjectiveFunctionI;

public class ModelCTAP implements ModelI {

	private final ObjectiveFunctionCTAP objF;
	private final List<ConstraintI> constraints;
	
	public ModelCTAP(ObjectiveFunctionCTAP objF,List<ConstraintI> constraints) {
		this.objF = objF;
		this.constraints = constraints;
	}
	
	@Override
	public ObjectiveFunctionCTAP getObjectiveFunction() {
		return this.objF;
	}

	@Override
	public List<ConstraintI> getConstraints() {
		return this.constraints;
	}
}

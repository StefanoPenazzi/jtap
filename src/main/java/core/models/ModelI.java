package core.models;

import java.util.List;

public interface ModelI {
	
	public ObjectiveFunctionI getObjectiveFunction();
	public List<ConstraintI> getConstraints();

}

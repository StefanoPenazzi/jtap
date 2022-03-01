package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class ActivityLocationCostParameter implements ParameterI<Integer> {
	
	private final double[][] parameter;
	private final  List<List<Integer>>  parameterDescription;
	private final String ID = "ActivityLocationCostParameter";
	
	public ActivityLocationCostParameter(double[][] parameter,
			List<List<Integer>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}

	@Override
	public Object getArrayParameter() {
		return this.parameter;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getId() {
		return this.ID;
	}

	@Override
	public List<List<Integer>> getParameterDescription() {
		return this.parameterDescription;
	}

}

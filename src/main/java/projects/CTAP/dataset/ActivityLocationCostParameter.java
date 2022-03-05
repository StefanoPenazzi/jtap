package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class ActivityLocationCostParameter implements ParameterI<Integer> {
	
	private double[][] parameter;
	private List<List<Integer>>  parameterDescription;
	private  String id = "ActivityLocationCostParameter";
	private String description = "";
	
	public ActivityLocationCostParameter(double[][] parameter,
			List<List<Integer>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public ActivityLocationCostParameter() {}

	@Override
	public Object getParameter() {
		return this.parameter;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public List<List<Integer>> getParameterDescription() {
		return this.parameterDescription;
	}

}

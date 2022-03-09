package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class ActivityLocationCostParameter implements ParameterI<Long> {
	
	private float[][] parameter;
	private List<List<Long>>  parameterDescription;
	private  String id = "ActivityLocationCostParameter";
	private String description = "ActivityId - LocationId";
	
	public ActivityLocationCostParameter(float[][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public ActivityLocationCostParameter() {}

	@Override
	public float[][] getParameter() {
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
	public List<List<Long>> getParameterDescription() {
		return this.parameterDescription;
	}

}

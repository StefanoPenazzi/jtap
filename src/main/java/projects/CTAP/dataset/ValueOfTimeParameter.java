package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class ValueOfTimeParameter implements ParameterI<Long> {

	private double[] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "ValueOfTimeParameter";
	private String description = "Agent_id";
	
	public ValueOfTimeParameter(double[] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public ValueOfTimeParameter() {}
	
	@Override
	public double[] getParameter() {
		return this.parameter;
	}

	@Override
	public String getDescription() {
		return this.description;
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
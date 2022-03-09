package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public final class TimeDurationParameter implements ParameterI<Long> {
	
	private double[][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "TimeDurationParameter";
	private String description = "Agent-Activity";
	
	public TimeDurationParameter(double[][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public TimeDurationParameter() {}

	@Override
	public double[][] getParameter() {
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

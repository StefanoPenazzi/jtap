package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class TimeRelatedBudgetParameter implements ParameterI<Long> {

	private double[] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "TimeRelatedBudgetParameter";
	private String description = "";
	
	public TimeRelatedBudgetParameter(double[] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public TimeRelatedBudgetParameter() {}
	
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
	public List<List<Long>> getParameterDescription() {
		return this.parameterDescription;
	}

}

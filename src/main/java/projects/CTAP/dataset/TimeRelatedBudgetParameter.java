package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class TimeRelatedBudgetParameter implements ParameterI<Long> {

	private final double[] parameter;
	private final  List<List<Long>>  parameterDescription;
	private final String ID = "TimeRelatedBudgetParameter";
	
	public TimeRelatedBudgetParameter(double[] parameter,
			List<List<Long>>  parameterDescription) {
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
	public List<List<Long>> getParameterDescription() {
		return this.parameterDescription;
	}

}

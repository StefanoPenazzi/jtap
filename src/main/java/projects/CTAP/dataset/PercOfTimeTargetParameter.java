package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class PercOfTimeTargetParameter implements ParameterI<Long> {
	
	private double[][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "PercOfTimeTargetParameter";
	private String description = "";
	
	public PercOfTimeTargetParameter(double[][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public PercOfTimeTargetParameter() {}

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

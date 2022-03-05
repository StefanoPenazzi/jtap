package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class DurationDiscomfortParameter implements ParameterI<Long> {

	private double[][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "DurationDiscomfortParameter";
	private String description = "";
	
	public DurationDiscomfortParameter(double[][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public DurationDiscomfortParameter() {}
	
	@Override
	public Object getParameter() {
		return this.parameter;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
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

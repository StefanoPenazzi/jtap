package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class AgentHomeLocationParameter implements ParameterI<Long> {

	private int[][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "AgentHomeLocationParameterr";
	private String description = "Agent_id - Location_id";
	
	public AgentHomeLocationParameter(int[][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public AgentHomeLocationParameter() {}
	
	@Override
	public int[][] getParameter() {
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

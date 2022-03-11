package projects.CTAP.dataset;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.dataset.ParameterI;

public class AttractivenessParameter implements ParameterI<Long> {

	private float[][][][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "AttractivenessParameter";
	private String description = "Agent_id - CitieDs_id - Activity_id - Time";
	
	public AttractivenessParameter(float[][][][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public AttractivenessParameter() {}
	
	@Override
	public float[][][][] getParameter() {
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

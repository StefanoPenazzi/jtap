package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class Ds2OsPathParameter implements ParameterI<Long>  {

	private List<Long>[][][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "Ds2OsPathParameter";
	private String description = "Projection-CityDs-CityDs";
	
	public Ds2OsPathParameter(List<Long>[][][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public Ds2OsPathParameter() {}
	
	@Override
	public List<Long>[][][] getParameter() {
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
	public List<List<Long>>  getParameterDescription() {
		return this.parameterDescription; 
	}

}

package projects.CTAP.dataset;

import java.util.List;

import core.dataset.ParameterI;

public class Os2DsPathParameter implements ParameterI<Long>  {

	private List<Long>[][][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "Os2DsPathParameter";
	private String description = "Projection-CityDs-CityDs";
	
	public Os2DsPathParameter(List<Long>[][][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public Os2DsPathParameter() {}
	
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

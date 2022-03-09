package projects.CTAP.dataset;

import java.util.List;
import java.util.Map;

import core.dataset.ParameterI;

public final class Os2DsTravelCostParameter implements ParameterI<Long> {

	private double[][][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "Os2DsTravelCostParameter";
	private String description = "Projection-CityOs-CityDs";
	
	
	public Os2DsTravelCostParameter(double[][][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public Os2DsTravelCostParameter() {}
	
	@Override
	public double[][][] getParameter() {
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

package projects.CTAP.dataset;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import core.dataset.ParameterI;

public final class Ds2DsTravelCostParameter implements ParameterI<Long> {

	private double[][][] parameter;
	private List<List<Long>>  parameterDescription;
	private String id = "Ds2DsTravelCostParameter";
	private String description = "";
	
	public Ds2DsTravelCostParameter(double[][][] parameter,
			List<List<Long>>  parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public Ds2DsTravelCostParameter() {}
	
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
	public List<List<Long>>  getParameterDescription() {
		return this.parameterDescription; 
	}

}

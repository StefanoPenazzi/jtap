package projects.CTAP.dataset;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import core.dataset.ParameterI;

public final class Ds2OsTravelCostParameter implements ParameterI<Long>{

	private double[][][] parameter;
	private List<List<Long>> parameterDescription;
	private String id = "Ds2OsTravelCostParameter";
	private String description = "";
	
	public Ds2OsTravelCostParameter(double[][][] parameter,
			List<List<Long>> parameterDescription) {
		this.parameter = parameter;
		this.parameterDescription = parameterDescription;
	}
	
	public Ds2OsTravelCostParameter() {}
	
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

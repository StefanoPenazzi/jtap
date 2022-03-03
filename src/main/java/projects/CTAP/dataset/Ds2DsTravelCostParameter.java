package projects.CTAP.dataset;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import core.dataset.ParameterI;

public final class Ds2DsTravelCostParameter implements ParameterI<Long> {

	private final double[][][] parameter;
	private final  List<List<Long>>  parameterDescription;
	private final String ID = "Ds2DsTravelCostParameter";
	
	public Ds2DsTravelCostParameter(double[][][] parameter,
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		return this.ID;
	}

	@Override
	public List<List<Long>>  getParameterDescription() {
		return this.parameterDescription; 
	}

}

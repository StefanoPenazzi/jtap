package projects.CTAP.dataset;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import core.dataset.ParameterI;

public final class Ds2DsParameter implements ParameterI {

	private final double[][][] parameter;
	private final  List<List<Object>>  parameterDescription;
	private final String ID = "Ds2DsParameter";
	
	public Ds2DsParameter(double[][][] parameter,
			List<List<Object>>  parameterDescription) {
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
	public List<List<Object>>  getParameterDescription() {
		return this.parameterDescription; 
	}

}

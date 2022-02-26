package projects.CTAP.dataset;

import java.util.List;
import java.util.Map;

import core.dataset.ParameterI;

public final class Os2DsParameter implements ParameterI<String> {

	private final double[][][] parameter;
	private final List<List<String>>  parameterDescription;
	private final String ID = "Os2DsParameter";
	
	
	public Os2DsParameter(double[][][] parameter,
			List<List<String>>  parameterDescription) {
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
	public List<List<String>> getParameterDescription() {
		return this.parameterDescription;
	}
}

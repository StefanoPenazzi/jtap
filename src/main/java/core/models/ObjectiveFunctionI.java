package core.models;

public interface ObjectiveFunctionI {
	
	public double getValue(double[] vars);
	public int getVariablesLength();

}

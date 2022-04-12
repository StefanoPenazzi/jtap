package projects.CTAP.attractiveness;

public interface AttractivenessModelI {
	
	public Double getAttractiveness(Double[] params, Double[] variables);
	public Double getAttractiveness(Double[] variables,Integer agentId, String activity);


}

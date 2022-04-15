package projects.CTAP.attractiveness.normalized;

import java.io.IOException;

import config.Config;

public class AttractivenessModelImpl_v1 extends DefaultAttractivenessModelImpl{

	public AttractivenessModelImpl_v1(Config config) throws IOException {
		super(config);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Double getAttractiveness(Double[] params, Double[] variables) {
		return seasonalitySummerSinglePeakSineFunction(params[0]*variables[0],variables[2]);
	}
	
	@Override
	public Double getAttractiveness(Double[] variables,Integer agentId, String activity) {
		return getAttractiveness(parametersMap.get(agentId).get(activity),variables);
	}


}

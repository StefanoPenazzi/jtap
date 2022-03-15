package core.population;

import projects.CTAP.dataset.Dataset;

public interface AgentFactoryI {
	
	public AgentI run(Long agentId,Long homeLocationId,Dataset dataset);

}

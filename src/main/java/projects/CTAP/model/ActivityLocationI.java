package projects.CTAP.model;

import java.util.List;

import projects.CTAP.dataset.Dataset;

public interface ActivityLocationI {
	
	public List<int[][]> run(Long agentId, Long locationId, Dataset dataset);

}

package projects.CTAP.dataset;

import java.util.List;
import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;
import core.graph.population.StdAgentNodeImpl;

public class LocationPerceptionParameterFactory implements ParameterFactoryI{

	private final List<StdAgentNodeImpl> agents;
	private final List<List<Long>> parameterDescription;
	
	public LocationPerceptionParameterFactory(List<StdAgentNodeImpl> agents,List<List<Long>> parameterDescription) {
		this.agents = agents;
		this.parameterDescription = parameterDescription;
	}
	
	@Override
	public ParameterI run() {
		
		return null;
		
	}

}
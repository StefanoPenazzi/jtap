package projects.CTAP.dataset;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;
import core.graph.population.StdAgentNodeImpl;

public class ValueOfTimeParameterFactory implements ParameterFactoryI{

	private final List<StdAgentNodeImpl> agents;
	private final List<List<Long>> parameterDescription;
	
	public ValueOfTimeParameterFactory(List<StdAgentNodeImpl> agents,List<List<Long>> parameterDescription) {
		this.agents = agents;
		this.parameterDescription = parameterDescription;
	}
	
	@Override
	public ParameterI run() {
		
		double[] parameter = new double[this.parameterDescription.get(0).size()];
		
	    Map<Long, StdAgentNodeImpl> map = agents.stream().collect(Collectors.toMap(StdAgentNodeImpl::getId,Function.identity()));
	    int ii = 0;
	    for(Long i: parameterDescription.get(0)) {
	    	parameter[ii] = map.get(i).getValueOfTime();
	    	ii++;
	    }
		return new ValueOfTimeParameter(parameter, parameterDescription );
		
	}

}

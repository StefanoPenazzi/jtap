package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.dataset.ParameterFactoryI;
import core.graph.population.StdAgentNodeImpl;

public final class AgentParametersFactory {
	
	public static List<? extends ParameterFactoryI> getAgentParameterFactories(List<Long> agentsIds) throws Exception{
		
		List<ParameterFactoryI> res = new ArrayList<>();
		
		List<StdAgentNodeImpl> agents = data.external.neo4j.Utils.importNodes(StdAgentNodeImpl.class);
		List<List<Long>> parameterDescription = new ArrayList<>() {{
			add(agentsIds);
		}};
		
		res.add(new TimeRelatedBudgetParameterFactory(agents,parameterDescription));
		res.add(new MonetaryBudgetParameterFactory(agents,parameterDescription));
		res.add(new ValueOfTimeParameterFactory(agents,parameterDescription));
		
		return res;
		
	}

}

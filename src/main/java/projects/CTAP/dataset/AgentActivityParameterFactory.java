package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.dataset.ParameterFactoryI;
import core.graph.population.AgentActivityLink;
import core.graph.population.StdAgentNodeImpl;

public class AgentActivityParameterFactory {
	
    public static List<? extends ParameterFactoryI> getAgentActivityParameterFactories(List<Long> agentsIds,List<Long> activitiesIds) throws Exception{
		
		List<ParameterFactoryI> res = new ArrayList<>();
		List<List<Long>> parameterDescription = new ArrayList<>() {{
			add(agentsIds);
			add(activitiesIds);
		}};
		List<AgentActivityLink> links = data.external.neo4j.Utils.importLinks(AgentActivityLink.class);
		
		res.add(new PercOfTimeTargetParameterFactory(links,parameterDescription));
		res.add(new TimeDurationParameterFactory(links,parameterDescription));
		res.add(new DurationDiscomfortParameterFactory(links,parameterDescription));
		
		return res;
		
	}

}

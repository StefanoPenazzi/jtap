package projects.CTAP.dataset;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;
import core.graph.population.AgentActivityLink;

public class TimeDurationParameterFactory implements ParameterFactoryI{

	private final List<AgentActivityLink> agentsActivities;
	private final List<List<Long>> ids;
	
	public TimeDurationParameterFactory(List<AgentActivityLink> agentsActivities,List<List<Long>> ids) {
		this.agentsActivities = agentsActivities;
		this.ids = ids;
	}
	
	@Override
	public ParameterI run() {
		
		double[][] parameter = new double[ids.get(0).size()][ids.get(1).size()];
		Map<String,Double> map = agentsActivities.stream()
				.collect(Collectors.toMap(x -> x.getAgentId().toString()+x.getActivityId().toString(),x -> x.getTimeDuration()));
		int ii = 0;
		int jj = 0;
		for(Long i: ids.get(0)) {
			for(Long j: ids.get(1)) {
				parameter[ii][jj] = map.get(i.toString()+j.toString());
				jj++;
			}
			ii++;
			jj = 0;
		}
		
		return new TimeDurationParameter(parameter,ids);
	}

}

package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;
import core.graph.population.AgentActivityLink;

public class AgentHomeLocationParameterFactory implements ParameterFactoryI {
	
	
	private final List<List<Long>> ids;

	public AgentHomeLocationParameterFactory(List<Long> agents_ids,List<Long> cities_ids) {
		this.ids = new ArrayList<>() {
			{
				add(agents_ids);
				add(cities_ids);
			}
		};
	}
	
	@Override
	public ParameterI run() {
		
		int[][] parameter = new int[ids.get(0).size()][ids.get(1).size()];
		
		
		StringBuilder agents = new StringBuilder();
		agents.append(" [");
		for (Long i : this.ids.get(0)) {
			agents.append("");
			agents.append(Long.toString(i));
			agents.append(",");
		}
		agents.setLength(agents.length() - 1);
		agents.append("] ");
		
		StringBuilder cities = new StringBuilder();
		cities.append(" [");
		for (Long i : this.ids.get(1)) {
			cities.append("");
			cities.append(Long.toString(i));
			cities.append(",");
		}
		cities.setLength(cities.length() - 1);
		cities.append("] ");

		
		String query = "MATCH (n:AgentNode)-[r:AgentGeoLink]->(m:CityNode)" +
		                 " WHERE n.agent_id IN " + agents.toString() + " AND m.city_id IN " + cities.toString()
		                 +" RETURN n.agent_id,m.city_id,r.size";
		
		List<Record> queryRes = null;
		try {
			queryRes = data.external.neo4j.Utils.runQuery(query, AccessMode.READ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String,Integer> map = new HashMap<>();
		for(Record rec:queryRes) {
			String key = String.valueOf(rec.values().get(0).asInt())+
					String.valueOf(rec.values().get(1).asInt());
			map.put( key,rec.values().get(2).asInt());
		}
		
		for(int i=0;i<this.ids.get(0).size();i++) {
			for(int j=0;j<this.ids.get(1).size();j++) {
				String key = this.ids.get(0).get(i).toString()+
						this.ids.get(1).get(j).toString();
				parameter[i][j] = map.get(key);	
			}
		}
		
		return new AgentHomeLocationParameter(parameter,ids);
	}

}

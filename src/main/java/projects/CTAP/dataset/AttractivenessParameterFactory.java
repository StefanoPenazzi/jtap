package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;

public class AttractivenessParameterFactory implements ParameterFactoryI {

	private final List<List<Long>> parameterDescription;
	
	public AttractivenessParameterFactory(List<Long> agents_ids,List<Long> activities_ids,List<Long> citiesDs_ids,List<Long> time) {
		this.parameterDescription = new ArrayList<>() {{
			add(agents_ids);
			add(citiesDs_ids);
			add(activities_ids);
			add(time);
		}};
	}
	
	@Override
	public ParameterI run() {
		
		 AttractivenessParameter res = null;
		
		double[][][][] parameter = new double[this.parameterDescription.get(0).size()]
				[this.parameterDescription.get(1).size()][this.parameterDescription.get(2).size()][this.parameterDescription.get(3).size()];
		
		StringBuilder cities = new StringBuilder();
		cities.append(" [");
		for(Long i:this.parameterDescription.get(1)) {
			cities.append("'");
			//cities.append(Long.toString(i));
			cities.append("',");
		}
		cities.setLength(cities.length() - 1);
		cities.append("] ");
		
		/*
		 * String query =
		 * "match (n:AgentNode)-[r:AttractivenessNormalizedLink]->(m:CityNode)" +
		 * " where m.city_id IN " + cities.toString()
		 * +" return n.agent_id,r.time,r.activity_id," + "r.attractiveness,m.city_id";
		 */
		
		String query = "match (n:AgentNode)-[r:AttractivenessNormalizedLink]->(m:CityNode)"
				+ " where m.city IN ['Paris','Marseille'] return n.agent_id,r.time,r.activity_id,"
				+ "r.attractiveness,m.city";
		
		List<Record> queryRes = null;
		try {
			queryRes = data.external.neo4j.Utils.runQuery(query, AccessMode.READ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Record rec: queryRes) {
			Integer i = rec.values().get(0).asInt();
			System.out.println();
		}
		return res;
	}

}

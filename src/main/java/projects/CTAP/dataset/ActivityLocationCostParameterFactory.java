package projects.CTAP.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;

public class ActivityLocationCostParameterFactory implements ParameterFactoryI{

	private final List<List<Long>> ids;
	
	public ActivityLocationCostParameterFactory (List<List<Long>> ids) {
		this.ids = ids;
	}
	
	@Override
	public ParameterI run() {
		
		float[][] parameter = new float[ids.get(0).size()][ids.get(1).size()];
		
		StringBuilder cities = new StringBuilder();
		cities.append(" [");
		for (Long i : this.ids.get(1)) {
			cities.append("");
			cities.append(Long.toString(i));
			cities.append(",");
		}
		cities.setLength(cities.length() - 1);
		cities.append("] ");
		
		StringBuilder activities = new StringBuilder();
		activities.append(" [");
		for (Long i : this.ids.get(1)) {
			activities.append("");
			activities.append(Long.toString(i));
			activities.append(",");
		}
		activities.setLength(cities.length() - 1);
		activities.append("] ");

		
		String query = "MATCH (n:ActivityNode)-[r:ActivityCityLink]->(m:CityNode)" +
		                 " WHERE m.city_id IN " + cities.toString() + " AND n.activity_id IN " + activities.toString()
		                 +" RETURN n.activity_id,m.city_id,r.cost";
		
		List<Record> queryRes = null;
		try {
			queryRes = data.external.neo4j.Utils.runQuery(query, AccessMode.READ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String,Float> map = new HashMap<>();
		for(Record rec:queryRes) {
			String key = String.valueOf(rec.values().get(0).asInt())+
					String.valueOf(rec.values().get(1).asInt());
			map.put( key,rec.values().get(2).asFloat());
		}
		
		for(int i=0;i<this.ids.get(0).size();i++) {
			for(int j=0;j<this.ids.get(1).size();j++) {
				String key = this.ids.get(0).get(i).toString()+
						this.ids.get(1).get(j).toString();
				parameter[i][j] = map.get(key);	
			}
		}
		
		return new ActivityLocationCostParameter(parameter,ids);
	}
}

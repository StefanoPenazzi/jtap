package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;

public class DestinationsProbDistParameterFactory implements ParameterFactoryI {

	private final List<List<Long>> parameterDescription;
	
	public DestinationsProbDistParameterFactory(List<Long> citiesOs_ids,
			List<Long> citiesDs_ids) {
		this.parameterDescription = new ArrayList<>() {
			{
				add(citiesOs_ids);
				add(citiesDs_ids);
			}
		};
	}
	
	@Override
	public ParameterI run() {
		
		double[][] parameter = new double[this.parameterDescription.get(0).size()][this.parameterDescription.get(1).size()];
 		
 		String query = "match (n:CityNode)-[r:DestinationProbLink]->(m:CityNode)"
 		                 +" return n.city_id,m.city_id,r.probability";
 		  
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
 			map.put(key,(float)rec.values().get(2).asDouble());
 		}
 		
 		
 		for(int i=0;i<this.parameterDescription.get(0).size();i++) {
 			for(int j=0;j<this.parameterDescription.get(1).size();j++) {
				String key = parameterDescription.get(0).get(i).toString()+
						parameterDescription.get(1).get(j).toString();
				parameter[i][j] = map.get(key);
			}
 		}
 		
 		return new DestinationsProbDistParameter(parameter,parameterDescription);
		
	}

}

package projects.CTAP.dataset;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.ListValue;

import com.google.inject.Inject;

import config.Config;
import core.dataset.LocationsMap;
import core.graph.geo.CityNode;

public class LocationsMapCTAP extends LocationsMap<CityNode> {

	private Config config;
	
	@Inject
	public LocationsMapCTAP(Config config) {
		super(config);
		this.config = config;
	}
	
	@Override
	public void initialization() {
		try {
			getLocationsFromNeo4J();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getLocationsFromNeo4J() throws Exception {
    	String query = "match (s:CityFacStatNode)<-[k]-(n:CityNode)<-[r:AgentGeoLink]-(m:AgentNode) with Collect(m.agent_id) AS agent, Collect(r.size) AS size, n,s return n,agent,size,s";
    	List<Record> records = data.external.neo4j.Utils.runQuery(config.getNeo4JConfig().getDatabase(),query,AccessMode.READ);
    	for(Record rec:records) {
			  List<Long> ag = ((ListValue)rec.values().get(1)).asList().stream().map(e -> (Long)e).collect(Collectors.toList());
			  List<Long> ags = ((ListValue)rec.values().get(2)).asList().stream().map(e -> (Long)e).collect(Collectors.toList());
			  Map<Long,Long> agents = IntStream.range(0,ag.size()).boxed().collect(Collectors.toMap(i -> ag.get(i), i -> ags.get(i)));
			  CityNode loc = core.graph.Utils.map2GraphElement(rec.values().get(0).asMap(),CityNode.class);
			  Map<String,Object> stat = rec.values().get(3).asMap();
			  //locationsMap.put(loc.getId(),new Location(loc,agents));
    	}
	}

}

package core.dataset;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.ListValue;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeGeoI;
import core.graph.NodeI;
import data.external.neo4j.Neo4jConnection;

public class LocationsMap <T extends NodeGeoI> implements DatasetMapI {
	
	Map<Long,Location> locationsMap  = new HashMap<>();
	private Config config;
	
	@Inject
	public LocationsMap(Config config) {
		this.config = config; 
	}
	
	public void getLocationsFromNeo4J(Class<T> locationClass) throws Exception {
    	String query = "match (n:CityNode)<-[r:AgentGeoLink]-(m:AgentNode) with Collect(m.agent_id) AS agent, Collect(r.size) AS size, n return n,agent,size";
    	List<Record> records = data.external.neo4j.Utils.runQuery(config.getNeo4JConfig().getDatabase(),query,AccessMode.READ);
    	for(Record rec:records) {
			  List<Long> ag = ((ListValue)rec.values().get(1)).asList().stream().map(e -> (Long)e).collect(Collectors.toList());
			  List<Long> ags = ((ListValue)rec.values().get(2)).asList().stream().map(e -> (Long)e).collect(Collectors.toList());
			  Map<Long,Long> agents = IntStream.range(0,ag.size()).boxed().collect(Collectors.toMap(i -> ag.get(i), i -> ags.get(i)));
			  T loc = core.graph.Utils.map2GraphElement(rec.values().get(0).asMap(),locationClass);
			  locationsMap.put(loc.getId(),new Location(loc,agents));
    	}
	    System.out.println("");
	}
	
	
	class Location{
		NodeGeoI node;
		Map<Long,Long> agents;
		public Location(NodeGeoI node, Map<Long,Long> agents) {
			this.node = node;
			this.agents = agents;
		}
		public NodeGeoI getNode() {
			return this.node;
		}
		public Map<Long,Long> getAgentsMap(){
			return this.agents;
		}
	}

}

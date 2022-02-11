package core.dataset;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.ListValue;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeGeoI;
import core.graph.NodeI;
import core.graph.geo.CityNode;
import data.external.neo4j.Neo4jConnection;

public class LocationsMap <T extends NodeGeoI> implements DatasetMapI {
	
	Map<Long,Location> locationsMap  = new ConcurrentHashMap<>();
	private Config config;
	public static final String LOCATIONS_MAP_KEY = "LocationsMap";
	
	
	public LocationsMap(Config config) {
		this.config = config; 
	}
	
	class Location{
		NodeGeoI node = null;
		Map<Long,Long> agents = new HashMap<>();
		Map<String,Number> statistics = new HashMap<>();
		public Location(NodeGeoI node, Map<Long,Long> agents, Map<String,Number> statistics) {
			this.node = node;
			this.agents = agents;
			this.statistics = statistics;
		}
		public Location() {}
		public NodeGeoI getNode() {
			return this.node;
		}
		public Map<Long,Long> getAgentsMap(){
			return this.agents;
		}
		public Map<String,Number> getStatistics(){
			return this.statistics;
		}
		public void addStatistics(String key, Number value) {
			this.statistics.put(key, value);
		}
		public void addAgent(Long key, Long value) {
			this.agents.put(key, value);
		}
	}


	@Override
	public String getKey() {
		return LOCATIONS_MAP_KEY;
	}

	@Override
	public void initialization() {
		
	}

}

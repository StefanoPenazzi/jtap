package core.graph.routing;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import data.external.neo4j.Neo4jConnection;

public final class RoutingManager {
	
	private Map<String,RoutingGraph>  routingGraphMap = new HashMap<>();
	private final Neo4jConnection conn;
	
	@Inject
	public RoutingManager() {
		conn = new Neo4jConnection();
	}
	
	public Object getRoute() {
		return null;
	}
	
	public void addNewRoutingGraph(RoutingGraph rg) {
		routingGraphMap.put(rg.getId(), rg);
	}
	
	public void close() throws Exception {
		conn.close();
	}

}

package core.graph.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import com.google.inject.Inject;

import config.Config;
import core.graph.NodeGeoI;
import data.external.neo4j.Neo4jConnection;

public final class RoutingManager {
	
	private Map<String,RoutingGraph>  routingGraphMap = new HashMap<>();
	private final Neo4jConnection conn;
	private final String database;
	private Config config;
	
	@Inject
	public RoutingManager(Config config) {
		conn = new Neo4jConnection();
		this.config = config;
		database = this.config.getNeo4JConfig().getDatabase();
	}
	
	
	public <T,K extends NodeGeoI>  List<Record> getSourceTargetRoute(String rg, T source, K target, String startPropertyKey, String startPropertyValue,
			 String endPropertyKey, String endPropertyValue, String weightProperty) throws Exception {
		
		RoutingGraph rgg = routingGraphMap.get(rg);
		if(rgg == null) {
			//eccezione
		}
		
		String query = "MATCH (source:"+ ((NodeGeoI)source).getLabels()[0] +" {"+startPropertyKey+": '"+startPropertyValue+"'}), (target:"+ ((NodeGeoI)target).getLabels()[0] +" {"+endPropertyKey+":'"+endPropertyValue+"'})"
    			+ " CALL gds.beta.shortestPath.astar.stream('"+rg+"', {"
				+ " sourceNode: id(source),"
				+ " targetNode: id(target),"
				+ " latitudeProperty: 'lat',"
				+ " longitudeProperty: 'lon',"
				+ " relationshipWeightProperty: '"+weightProperty+"'"
				+ "}) "
				+ "YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs "
				+ "RETURN "
				+ "index,"
				+ "gds.util.asNode(sourceNode)."+startPropertyKey+" AS sourceNodeName,"
				+ "gds.util.asNode(targetNode)."+endPropertyKey+" AS targetNodeName,"
				+ "totalCost,"
				+ "[nodeId IN nodeIds | gds.util.asNode(nodeId).id] AS nodeNames,"
				+ "costs "
				+ "ORDER BY index;";
	    
	    List<Record> res= data.external.neo4j.Utils.runQuery(conn,database,query,AccessMode.READ);
		return res;
	}
	
	//single source shortest path
	public <T extends NodeGeoI>  List<Record> getSSSP(String rg, T source, String sourcePropertyKey, String sourcePropertyValue,
			 String targetPropertyKey, String weightProperty) throws Exception {
		
		RoutingGraph rgg = routingGraphMap.get(rg);
		if(rgg == null) {
			//eccezione
		}
		
		String query = "MATCH (source:"+ ((NodeGeoI)source).getLabels()[0] +" {"+sourcePropertyKey+": '"+sourcePropertyValue+"'})\n"
				+ "CALL gds.alpha.shortestPath.deltaStepping.stream('"+rg+"',{\n"
				+ "   startNode: source,\n"
				+ "   relationshipWeightProperty: '"+weightProperty+"',\n"
				+ "   delta: 3.0\n"
				+ "})\n"
				+ "YIELD nodeId, distance\n"
				+ "RETURN gds.util.asNode(nodeId)."+targetPropertyKey+" AS target, distance AS cost\n"
				+ "ORDER BY target;";
	    
	    List<Record> res= data.external.neo4j.Utils.runQuery(conn,database,query,AccessMode.READ);
		return res;
	}
	
	//single source shortest path
	public <T extends NodeGeoI>  Map<String,Double> getSSSP_AsMap(String rg, T source, String sourcePropertyKey, String sourcePropertyValue,
			 String targetPropertyKey, String weightProperty) throws Exception {
	    
		List<Record> records = getSSSP(rg,source,sourcePropertyKey,sourcePropertyValue,targetPropertyKey,weightProperty);
		Map<String,Double > map = new HashMap<>();
		for(Record rec: records) {
			if(rec.values().get(0) != null) {
				map.put(rec.values().get(0).toString(),rec.values().get(1).asDouble());
			}
		}
		return map;
	}
	
	//TODO
	public <T extends NodeGeoI>  List<Map<String,Double>> getSSSP_AsMapParallel(String rg, List<T> source, List<String> sourcePropertyKey, List<String> sourcePropertyValue,
			 List<String> targetPropertyKey, List<String> weightProperty) throws Exception {
	    
		
		return null;
	}
	
	public void addNewRoutingGraph(RoutingGraph rg) throws Exception {
		routingGraphMap.put(rg.getId(), rg);
		if(!rg.cached()) {
			rg.graphCaching(conn,database);
		}
	}
	
	public void close() throws Exception {
		for(String ss:routingGraphMap.keySet()) {
			data.external.neo4j.Utils.runQuery(conn,database,"CALL gds.graph.drop('"+ss+"') YIELD graphName;",AccessMode.WRITE);
		}
		conn.close();
	}

}

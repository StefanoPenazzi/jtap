package core.graph.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import com.google.inject.Inject;

import core.graph.NodeGeoI;
import data.external.neo4j.Neo4jConnection;

public final class RoutingManager {
	
	private Map<String,RoutingGraph>  routingGraphMap = new HashMap<>();
	private final Neo4jConnection conn;
	private final String database;
	
	@Inject
	public RoutingManager() {
		conn = new Neo4jConnection();
		database = "france2";
	}
	
	
	public <T,K extends NodeGeoI>  List<Record> getRoute(String rg, T start, K end, String startPropertyKey, String startPropertyValue,
			 String endPropertyKey, String endPropertyValue) throws Exception {
		
		RoutingGraph rgg = routingGraphMap.get(rg);
		if(rgg == null) {
			//eccezione
		}
		
		
		//String query = "MATCH (source:"+nsLabel+" {"+nsPropertyKey+": "+nsPropertyValue+"}), (target:"+neLabel+" {"+nePropertyKey+":"+nePropertyValue+"})"
    	//		+ " CALL gds.beta.shortestPath.astar.stream('"+graphName+"', {"
		//		+ " sourceNode: id(source),"
		//		+ " targetNode: id(target),"
		//		+ " latitudeProperty: '"+latitudeProperty+"',"
		//		+ " longitudeProperty: '"+longitudeProperty+"',"
		//		+ " relationshipWeightProperty: '"+relationshipWeightProperty+"'"
		//		+ "}) "
		//		+ "YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs "
		//		+ "RETURN "
		//		+ "index,"
		//		+ "gds.util.asNode(sourceNode)."+nodeIdRes+" AS sourceNodeName,"
		//		+ "gds.util.asNode(targetNode)."+nodeIdRes+" AS targetNodeName,"
		//		+ "totalCost,"
		//		+ "[nodeId IN nodeIds | gds.util.asNode(nodeId)."+nodeIdRes+"] AS nodeNames,"
		//		+ "costs "
		//		+ "ORDER BY index;";
		
		
		String query = "MATCH (source:CityNode {city: 'Nancy'}), (target:CityNode {city:'Laval'})\n"
				+ "CALL gds.beta.shortestPath.astar.stream('train-intersections-graph-2', {\n"
				+ "    sourceNode: id(source),\n"
				+ "    targetNode: id(target),\n"
				+ "    latitudeProperty: 'lat',\n"
				+ "    longitudeProperty: 'lon',\n"
				+ "    relationshipWeightProperty: 'avg_travel_time'\n"
				+ "})\n"
				+ "YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs\n"
				+ "RETURN\n"
				+ "    index,\n"
				+ "    gds.util.asNode(sourceNode).node_osm_id AS sourceNodeName,\n"
				+ "    gds.util.asNode(targetNode).node_osm_id AS targetNodeName,\n"
				+ "    totalCost,\n"
				+ "    [nodeId IN nodeIds | gds.util.asNode(nodeId).id] AS nodeNames,\n"
				+ "    costs\n"
				+ "ORDER BY index";
	    
	    List<Record> res= data.external.neo4j.Utils.runQuery(conn,database,query,AccessMode.READ);
		return res;
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

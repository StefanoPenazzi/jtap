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
	public <T extends NodeGeoI>  List<Record> getSSSP(String rg, T source, String sourceIdKey, Long sourceId,
			 String targetIdKey, String weightProperty) throws Exception {
		
		RoutingGraph rgg = routingGraphMap.get(rg);
		if(rgg == null) {
			//eccezione
		}
		
		String query = "MATCH (source:"+ ((NodeGeoI)source).getLabels()[0] +" {"+sourceIdKey+": "+sourceId.toString()+"})\n"
				+ "CALL gds.alpha.shortestPath.deltaStepping.stream('"+rg+"',{\n"
				+ "   startNode: source,\n"
				+ "   relationshipWeightProperty: '"+weightProperty+"',\n"
				+ "   delta: 3.0\n"
				+ "})\n"
				+ "YIELD nodeId, distance\n"
				+ "RETURN gds.util.asNode(nodeId)."+targetIdKey+" AS target, distance AS cost\n"
				+ "ORDER BY target;";
	    
	    List<Record> res= data.external.neo4j.Utils.runQuery(conn,database,query,AccessMode.READ);
		return res;
	}
	
	public <T extends NodeGeoI>  List<Record> getSSSPWithPaths(String rg, T source, String sourceIdKey, Long sourceId,
			 String targetIdKey, String weightProperty) throws Exception {
		
		RoutingGraph rgg = routingGraphMap.get(rg);
		if(rgg == null) {
			//eccezione
		}
		
		String query = " MATCH (source:"+((NodeGeoI)source).getLabels()[0] +" {"+sourceIdKey+": "+sourceId.toString()+"})\n"
		+"CALL gds.beta.allShortestPaths.dijkstra.stream('"+rg+"', {\n"
		+ "    sourceNode: ID(source), \n"
		+ "    relationshipWeightProperty: '"+weightProperty+"' \n"
		+ "}) \n"
		+ "YIELD targetNode, totalCost, nodeIds \n"
 		+ "WHERE gds.util.asNode(targetNode)."+targetIdKey+" IS NOT NULL \n"
		+ "WITH COLLECT(nodeIds) AS paths, targetNode AS targetNode, totalCost AS totalCost \n"
		+ " UNWIND range(0,size(paths)-1) AS i \n"
		+ " WITH paths[i] as path,targetNode AS targetNode, totalCost AS totalCost \n"
		+ "  UNWIND range(0,size(path)-2) AS j \n"
		+ "  match (m)-[r]->(n) where ID(m)=path[j] and ID(n)=path[j+1] \n"
		+ "  with collect(ID(r)) as links_path,targetNode AS targetNode, totalCost AS totalCost \n"
		+ "  return gds.util.asNode(targetNode)."+targetIdKey+",totalCost,links_path \n";
	    
	    List<Record> res= data.external.neo4j.Utils.runQuery(conn,database,query,AccessMode.READ);
		return res;
	}

	
	//single source shortest path
	public <T extends NodeGeoI>  Map<Long,PathRes> getSSSP_AsMap(String rg, T source, String sourceIdKey, Long sourceId,
			 String targetIdKey, String weightProperty) throws Exception {
	    
		List<Record> records = getSSSP(rg,source,sourceIdKey,sourceId,targetIdKey,weightProperty);
		Map<Long,PathRes> map = new HashMap<>();
		for(Record rec: records) {
			if(!rec.values().get(0).isNull()) {
				map.put(rec.values().get(0).asLong(),new PathRes(rec.values().get(1).asDouble(),null));
			}
		}
		return map;
	}
	  
	public <T extends NodeGeoI>  Map<Long,PathRes> getSSSP_AsMapWithPaths(String rg, T source, String sourceIdKey, Long sourceId,
				 String targetIdKey, String weightProperty) throws Exception {
		    
		List<Record> records = getSSSPWithPaths(rg,source,sourceIdKey,sourceId,targetIdKey,weightProperty);
		Map<Long,PathRes> map = new HashMap<>();
		for(Record rec: records) {
			List<Long> linksPath = rec.values().get(2).asList().stream().map(e -> (Long)e).collect(Collectors.toList());;
			map.put(rec.values().get(0).asLong(),new PathRes(rec.values().get(1).asDouble(),linksPath));
			
		}
		return map;
	}
	
	//TODO
	public <T extends NodeGeoI>  List<Map<String,Double>> getSSSP_AsMapParallel(String rg, List<T> source, List<String> sourcePropertyKey, List<String> sourcePropertyValue,
			 List<String> targetPropertyKey, List<String> weightProperty) throws Exception {
	    
		
		return null;
	}
	
	public void addNewRoutingGraph(RoutingGraph rg) throws Exception {
		if(!routingGraphMap.containsKey(rg.getId())) {
			routingGraphMap.put(rg.getId(), rg);
			if(!rg.cached()) {
				rg.graphCaching(conn,database);
			}
		}
		else {
			System.out.println();
		}
	}
	
	public void close() throws Exception {
		for(String ss:routingGraphMap.keySet()) {
			data.external.neo4j.Utils.runQuery(conn,database,"CALL gds.graph.drop('"+ss+"') YIELD graphName;",AccessMode.WRITE);
		}
		conn.close();
	}

}

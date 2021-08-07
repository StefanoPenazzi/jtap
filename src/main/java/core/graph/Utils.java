package core.graph;

import java.util.List;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Node;

import data.external.neo4j.Neo4jConnection;

public class Utils {
	
	public static void createGraphCatalog(Neo4jConnection conn,String database,String graphName,String nodesQuery, String linksQuery) throws Exception {
		conn.query(database,
				"CALL gds.graph.create.cypher('"+graphName+"',"
						+ "'"+nodesQuery+"',"
								+ "'"+linksQuery+"')",AccessMode.WRITE );
	}
	
	public static void deleteGraphCatalog(Neo4jConnection conn,String database, String graphName) throws Exception { 
        conn.query(database,"CALL gds.graph.drop('"+graphName+"') YIELD graphName;",AccessMode.WRITE );
	}
	
	public static List<Record>  getShortestPathAStar(final String database,final String graphName,final String nsLabel,final String nsPropertyKey,final String nsPropertyValue,
			final String neLabel,final String nePropertyKey,final String nePropertyValue,
			final String latitudeProperty,final String longitudeProperty,final String relationshipWeightProperty,
			final String nodeIdRes) throws Exception {
		
		    String query = "MATCH (source:"+nsLabel+" {"+nsPropertyKey+": '"+nsPropertyValue+"'}), (target:"+neLabel+" {"+nePropertyKey+":'"+nePropertyValue+"'})"
        			+ " CALL gds.beta.shortestPath.astar.stream('"+graphName+"', {"
        			+ " sourceNode: id(source),"
        			+ " targetNode: id(target),"
        			+ " latitudeProperty: '"+latitudeProperty+"',"
        			+ " longitudeProperty: '"+longitudeProperty+"',"
        			+ " relationshipWeightProperty: '"+relationshipWeightProperty+"'"
        			+ "}) "
        			+ "YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs "
        			+ "RETURN "
        			+ "index,"
        			+ "gds.util.asNode(sourceNode)."+nodeIdRes+" AS sourceNodeName,"
        			+ "gds.util.asNode(targetNode)."+nodeIdRes+" AS targetNodeName,"
        			+ "totalCost,"
        			+ "[nodeId IN nodeIds | gds.util.asNode(nodeId)."+nodeIdRes+"] AS nodeNames,"
        			+ "costs "
        			+ "ORDER BY index;";
		    
		    List<Record> res= data.external.neo4j.Utils.run(database,query,AccessMode.READ);
		
		return res;
	}
	
	public static WeightedPath getShortestPathDijkstra(String weight,String graphName,final Node start, Node end) {
		return null;
	}
	
	public static List<Record> getCatalog(Neo4jConnection conn,String database) throws Exception{
		
		List<Record> res = null;
		res= conn.query(database," CALL gds.graph.list()"
			 		+ " YIELD graphName,database, nodeCount, relationshipCount, schema;",AccessMode.READ);
		return res;
	}

}

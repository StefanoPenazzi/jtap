package core.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Node;

import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.cross.CrossLink;
import core.graph.rail.RailLink;
import data.external.neo4j.Neo4jConnection;
import data.utils.geo.Gis;
import data.utils.io.CSV;
import data.utils.woods.kdTree.KdTree;

/**
 * @author stefanopenazzi
 *
 */
public class Utils {
	
	
	
	/**
	 * @param database
	 * @param graphName
	 * @param nsLabel
	 * @param nsPropertyKey
	 * @param nsPropertyValue
	 * @param neLabel
	 * @param nePropertyKey
	 * @param nePropertyValue
	 * @param latitudeProperty
	 * @param longitudeProperty
	 * @param relationshipWeightProperty
	 * @param nodeIdRes
	 * @return
	 * @throws Exception
	 */
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
		    
		    List<Record> res= data.external.neo4j.Utils.runQuery(database,query,AccessMode.READ);
		
		return res;
	}
	
	public static WeightedPath getShortestPathDijkstra(String weight,String graphName,final Node start, Node end) {
		return null;
	}
	
	/**
	 * @param conn
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public static List<Record> getCatalog(Neo4jConnection conn,String database) throws Exception{
		List<Record> res = null;
		res= conn.query(database," CALL gds.graph.list()"
			 		+ " YIELD graphName,database, nodeCount, relationshipCount, schema;",AccessMode.READ);
		return res;
	}
	
	/**
	 * @param <T>
	 * @param <K>
	 * @param database
	 * @param tempDirectory
	 * @param nodeDeparture
	 * @param propNodeDeparture
	 * @param nodeArrival
	 * @param propNodeArrival
	 * @throws Exception
	 */
	public static <T extends NodeGeoI,K extends NodeGeoI>  void setShortestDistCrossLink(String database,String tempDirectory,Class<T> nodeDeparture,String propNodeDeparture,Class<K> nodeArrival,String propNodeArrival) throws Exception {
		
		String labelNodeDeparture = null;
		String labelNodeArrival =null;
		if(nodeDeparture.isAnnotationPresent(Neo4JNodeElement.class) && 
				nodeArrival.isAnnotationPresent(Neo4JNodeElement.class)) {
			labelNodeDeparture = nodeDeparture.getAnnotation(Neo4JNodeElement.class).labels()[0];
			labelNodeArrival = nodeArrival.getAnnotation(Neo4JNodeElement.class).labels()[0];
			Boolean annotationFieldDeparture = FieldUtils.getFieldsListWithAnnotation(nodeDeparture, Neo4JPropertyElement.class).stream()
					.filter(f -> f.getAnnotation(Neo4JPropertyElement.class)
							.key().equals(propNodeDeparture)).toArray().length == 1? true:false;
			Boolean annotationFieldArrival = FieldUtils.getFieldsListWithAnnotation(nodeArrival, Neo4JPropertyElement.class).stream()
					.filter(f -> f.getAnnotation(Neo4JPropertyElement.class)
							.key().equals(propNodeArrival)).toArray().length == 1? true:false;
			if(!(annotationFieldDeparture && annotationFieldArrival)) {
				throw new Exception("Property not found");
			}
		}
		else {
			throw new Exception("Only classes annotated with @Neo4JNodeElement can be converted in nodes");
		}
	    
		List<Record> nodeDepartureRecords = null;
		try( Neo4jConnection conn = new Neo4jConnection()){  
			nodeDepartureRecords = conn.query(database,"MATCH (n:"+labelNodeDeparture+") RETURN n",AccessMode.READ);
		}
		
		List<Record> arrivalNodeRecords = null;
		try( Neo4jConnection conn = new Neo4jConnection()){  
			arrivalNodeRecords = conn.query(database,"MATCH (n:"+labelNodeArrival+") RETURN n",AccessMode.READ);
		}
		
		//KDTREE contains all the node that need to be connected with departure nodes.
		List<KdTree.Node> kdtNodes = new ArrayList<>();
		for(Record r: arrivalNodeRecords) {
			List<org.neo4j.driver.util.Pair<String, Value>> values = r.fields();
			for (org.neo4j.driver.util.Pair<String, Value> nameValue: values) {
			    if ("n".equals(nameValue.key())) { 
			        Value value = nameValue.value();
			        if(!value.get("lat").isNull() && !value.get("lon").isNull() 
			        		&& !value.get(propNodeArrival).isNull()) {
				        Double lat = value.get("lat").asDouble();
				        Double lon = value.get("lon").asDouble();
				        String sid = String.valueOf(value.get(propNodeArrival).asObject());
				        kdtNodes.add(new KdTree.Node(lat,lon,sid));
			        }
			    }
			}
		}
		
		//insert  arrivalNodes in a KD-TREE structure to facilitate the research of the closest to the departureNode
		KdTree kdt = new KdTree(2,kdtNodes);
		List<CrossLink> links = new ArrayList<>();
		
		for(Record r: nodeDepartureRecords) {
			Double lat = null; 
	        Double lon = null;
	        String sid = null;
			List<org.neo4j.driver.util.Pair<String, Value>> values = r.fields();
			for (org.neo4j.driver.util.Pair<String, Value> nameValue: values) {
			    if ("n".equals(nameValue.key())) {  // you named your node "p"
			        Value value = nameValue.value();
			        if(!value.get("lat").isNull() && !value.get("lon").isNull() 
			        		&& !value.get(propNodeDeparture).isNull()) {
			        	lat = value.get("lat").asDouble();
				        lon = value.get("lon").asDouble();
				        sid = String.valueOf(value.get(propNodeDeparture).asObject());
				        //find the closest node
				        KdTree.Node n = kdt.findNearest(new KdTree.Node(
								lat,lon,null));
				        int dist = Gis.longDist(lat,lon,n.getCoords()[0],n.getCoords()[1]);
						links.add(new CrossLink(sid,n.getValue().toString(),dist,0));
						break;	
			        }
			    }
			}
		}
		
		data.external.neo4j.Utils.insertLinks(database,tempDirectory,links
				,CrossLink.class,nodeDeparture,propNodeDeparture,"from",nodeArrival,propNodeArrival,"to");	
	}
	
	/**
	 * @param <T>
	 * @param database
	 * @param tempDirectory
	 * @param nodeDeparture
	 * @param propNodeDeparture
	 * @param nodeArrivalMap
	 * @throws Exception
	 */
	public static <T extends NodeGeoI> void setShortestDistCrossLink(String database,String tempDirectory,Class<T> nodeDeparture,String propNodeDeparture, Map<Class<? extends NodeGeoI>,String> nodeArrivalMap) throws Exception {
		
		Iterator it = nodeArrivalMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<Class<? extends NodeGeoI>,String> pair = (Map.Entry)it.next();
	    	setShortestDistCrossLink(database,tempDirectory,nodeDeparture,propNodeDeparture,pair.getKey(),pair.getValue());
	    }
	}

}

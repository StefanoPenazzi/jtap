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

import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.cross.CrossLink;
import data.external.neo4j.Neo4jConnection;
import data.utils.geo.Gis;
import data.utils.io.CSV;
import data.utils.woods.kdTree.KdTree;

/**
 * @author stefanopenazzi
 *
 */
public class Utils {
	
	private static final double WALKSPEED = 1.39;    //m/s
	private static final double CARSPEED = 13.9;    //m/s
	
	
    public static <T extends NodeI> void insertNodesIntoNeo4J(String database,List<T> nodes,
    		String tempDirectory,Class<T> nodeClass) throws Exception {
		data.external.neo4j.Utils.insertNodes(database,tempDirectory,nodes);
	}
	
	public static <T extends NodeI> void insertNodesIntoNeo4J(String database,String fileCsv,
			String tempDirectory,Class<T> nodeClass) throws Exception {
		
		List<T> nodes = CSV.getListByHeaders(new File(fileCsv),nodeClass);
		insertNodesIntoNeo4J(database,nodes,tempDirectory,nodeClass); 
	}
	
	
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
	
	    String query = "MATCH (source:"+nsLabel+" {"+nsPropertyKey+": "+nsPropertyValue+"}), (target:"+neLabel+" {"+nePropertyKey+":"+nePropertyValue+"})"
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
	public static <T extends NodeGeoI,K extends NodeGeoI>  void setShortestDistCrossLink(String database,String tempDirectory,Class<T> nodeDeparture,String propNodeDeparture,Class<K> nodeArrival,String propNodeArrival,Boolean twoWays) throws Exception {
		
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
		List<CrossLink> links2Dir = new ArrayList<>();
		
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
				        int avgTravelTime = (int)(dist/CARSPEED);
				        links.add(new CrossLink(sid,n.getValue().toString(),dist,avgTravelTime));
						if(twoWays) {
							links2Dir.add(new CrossLink(n.getValue().toString(),sid,dist,avgTravelTime));
						}
						break;	
			        }
			    }
			}
		}
		
		data.external.neo4j.Utils.insertLinks(database,tempDirectory,links
				,CrossLink.class,nodeDeparture,propNodeDeparture,"from",nodeArrival,propNodeArrival,"to");	
		if(twoWays) {
			data.external.neo4j.Utils.insertLinks(database,tempDirectory,links2Dir
					,CrossLink.class,nodeArrival,propNodeArrival,"from",nodeDeparture,propNodeDeparture,"to");
		}
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
	public static <T extends NodeGeoI> void setShortestDistCrossLink(String database,String tempDirectory,Class<T> nodeDeparture,String propNodeDeparture, Map<Class<? extends NodeGeoI>,String> nodeArrivalMap,Boolean twoWays) throws Exception {
		
		Iterator it = nodeArrivalMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<Class<? extends NodeGeoI>,String> pair = (Map.Entry)it.next();
	    	setShortestDistCrossLink(database,tempDirectory,nodeDeparture,propNodeDeparture,pair.getKey(),pair.getValue(),twoWays);
	    }
	}
	
	

}

package data.external.rail;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.javatuples.Pair;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;

import config.Config;
import data.external.neo4j.Neo4jConnection;
import data.utils.geo.Gis;
import data.utils.io.CSV;
import data.utils.woods.kdTree.KdTree;


public final class Utils {
	
	public static String STOPSFILE = "/stops.csv";
	public static String RAILLINKSFILE = "/links.csv";
	public static String TRANSFERLINKSFILE = "/transfers.csv";
	public static String RAILNODE = "RailNode";
	public static String RAILLINK = "RailLink";
	public static String RAILTRANSFERLINK = "RailTransferLink";
	
	/**
	 * 
	 * https://stackoverflow.com/questions/45620367/unable-to-load-csv-file-into-neo4j
	 * 
	 * @param gtfs
	 * @param database
	 * @param nodes : array containing the nodes types that have to be connected with the GTFS network
	 * @throws Exception
	 */
	public static void insertGTFSintoNEO4J(GTFS gtfs,String database,Config config,String... nodes) throws Exception {
		List<RailLink> railLinks = getRailLinks(gtfs);
		List<RailTransferLink> transferLinks = getTransferLinks(gtfs);
		String tempDirectory = config.getGeneralConfig().getTempDirectory();
		//LOAD USING CSV
		//NODES
		//save csv file with the nodes in the temp directory
		CSV.writeTo(new File(tempDirectory+STOPSFILE),gtfs.getStops());
		//RAIL_LINKS
		CSV.writeTo(new File(tempDirectory+RAILLINKSFILE),railLinks);
		//TRANSFERS LINKS
		CSV.writeTo(new File(tempDirectory+TRANSFERLINKSFILE),transferLinks);
		
		
		try( Neo4jConnection conn = new Neo4jConnection()){  
			
			//NODES
			conn.query(database,"LOAD CSV WITH HEADERS FROM \"file:///"+tempDirectory+STOPSFILE+"\" AS csvLine "
					+ "CREATE (p:"+RAILNODE+" {stop_id: csvLine.STOP_ID, stop_name: csvLine.STOP_NAME,"
					+ "stop_lat: toFloat(csvLine.STOP_LAT), stop_lon: toFloat(csvLine.STOP_LON)})",AccessMode.WRITE );
			//LINKS RAIL
			conn.query(database,"LOAD CSV WITH HEADERS FROM \"file:///"+tempDirectory+RAILLINKSFILE+"\" AS csvLine "
					+ "MATCH (sf:"+RAILNODE+" {stop_id:csvLine.STOP_FROM}),(st:"+RAILNODE+" {stop_id:csvLine.STOP_TO})"
					+ "CREATE (sf)-[:RailLink {avg_travel_time:toFloat(csvLine.AVG_TRAVEL_TIME),connections_per_day:toInteger(csvLine.CONNECTIONS_PER_DAY),"
					+ "first_dep_time:csvLine.FIRST_DEP_TIME,last_dep_time:csvLine.LAST_DEP_TIME}]->(st)",AccessMode.WRITE );
			//LINKS TRANSFER
			conn.query(database,"LOAD CSV WITH HEADERS FROM \"file:///"+tempDirectory+TRANSFERLINKSFILE+"\" AS csvLine "
					+ "MATCH (sf:"+RAILNODE+" {stop_id:csvLine.STOP_FROM}),(st:"+RAILNODE+" {stop_id:csvLine.STOP_TO})"
					+ "CREATE (sf)-[:TransferLink {avg_travel_time:toFloat(csvLine.AVG_TRAVEL_TIME)}]->(st)",AccessMode.WRITE );
	    }
		
		connectRailNodes(database,tempDirectory,nodes);
		
	}
	
	public static void deleteGTFS(String database) throws Exception {
         try( Neo4jConnection conn = new Neo4jConnection()){  
			//delete from the db all the rail nodes and links
			conn.query(database,"MATCH (n:"+RAILNODE+") DETACH DELETE n;",AccessMode.WRITE );
         }
	}
	
	/**
	 * @param database
	 * @param nodes
	 * @throws Exception 
	 */
	public static void connectRailNodes(String database,String tempDirectory,String... nodes) throws Exception {
		
		List<Record> railNodeRecords = null;
		try( Neo4jConnection conn = new Neo4jConnection()){  
			railNodeRecords = conn.query(database,"MATCH (n:"+RAILNODE+") RETURN n",AccessMode.READ);
		}
		
		for(String node : nodes) {
			
			List<Record> otherNodeRecords = null;
			StringBuilder sb = new StringBuilder();  
			
			try( Neo4jConnection conn = new Neo4jConnection()){  
				otherNodeRecords = conn.query(database,"MATCH (n:"+node+") RETURN n",AccessMode.READ);
			}
			
			//KDTREE contains all the node that need to be connected with railnodes.
			List<KdTree.Node> kdtNodes = new ArrayList<>();
			for(Record r: otherNodeRecords) {
				
				List<org.neo4j.driver.util.Pair<String, Value>> values = r.fields();
				for (org.neo4j.driver.util.Pair<String, Value> nameValue: values) {
				    if ("n".equals(nameValue.key())) {  // you named your node "p"
				        Value value = nameValue.value();
				        Double lat = value.get("lat").asDouble();
				        Double lon = value.get("lon").asDouble();
				        Integer sid = value.get("node_osm_id").asInt();
				        kdtNodes.add(new KdTree.Node(lat,lon,sid));
				    }
				}
			}
			
			
			KdTree kdt = new KdTree(2,kdtNodes);
			List<RailRoadLink> links = new ArrayList<>();
			
			for(Record r: railNodeRecords) {
				Double lat = null; 
		        Double lon = null;
		        String sid = null;
				List<org.neo4j.driver.util.Pair<String, Value>> values = r.fields();
				for (org.neo4j.driver.util.Pair<String, Value> nameValue: values) {
				    if ("n".equals(nameValue.key())) {  // you named your node "p"
				        Value value = nameValue.value();
				        lat = value.get("stop_lat").asDouble();
				        lon = value.get("stop_lon").asDouble();
				        sid = value.get("stop_id").asString();
				        //find the closest node to the railnode
				        KdTree.Node n = kdt.findNearest(new KdTree.Node(
								lat,lon,null));
				        int dist = Gis.longDist(lat,lon,n.getCoords()[0],n.getCoords()[1]);
						links.add(new RailRoadLink(sid,n.getValue().toString(),dist));
						break;	
				    }
				}
				System.out.println();
				
			}
			
			CSV.writeTo(new File(tempDirectory+"/Rail"+node+".csv"),links);
			try(Neo4jConnection conn = new Neo4jConnection()){ 
				//TODO metti .csv nel file non solo nodew
				conn.query(database,"LOAD CSV WITH HEADERS FROM \"file:///"+tempDirectory+"/Rail"+node+".csv"+"\" AS csvLine "
						+ "MATCH (sf:"+RAILNODE+" {stop_id:csvLine.STOP_FROM}),(st:"+node+" {node_osm_id:toInteger(csvLine.ROAD_TO)})"
						+ "CREATE (sf)-[:Rail"+node+"Link {distance:toInteger(csvLine.DISTANCE)}]->(st)",AccessMode.WRITE );
			}
		}
		System.out.println();
	}
	
	public static Map<Pair<String,String>, List<Connection>> getConnections(GTFS gtfs){
		List<Connection> connections = new ArrayList<>();
		List<StopTime> stopTime = gtfs.getStopTimes();
		Map<String, List<StopTime>> tripStops = stopTime.stream()
				 .sorted(Comparator.comparing(StopTime::getDepartureTime))
				 .collect(Collectors.groupingBy(StopTime::getTripId));
		
		for (var entry : tripStops.entrySet()) {
			List<StopTime> st = entry.getValue();
		    for(int j=0;j<st.size()-1;j++) {
		    	Connection c = new Connection(st.get(j).getStopId(),
		    			st.get(j+1).getStopId(),
		    			st.get(j).getDepartureTime(),
		    			st.get(j+1).getArrivalTime().toSecondOfDay()-
	                    		  st.get(j).getDepartureTime().toSecondOfDay());
		    	connections.add(c);
		    }
		}
		Map<Pair<String,String>, List<Connection>> connectionsMap = connections.stream()
				  .collect(Collectors.groupingBy(c -> new Pair<String,String>(c.getFrom(), c.getTo())));
		return connectionsMap;
	}
	
	
	
	public static List<RailLink> getRailLinks(GTFS gtfs){
		List<RailLink> links = new ArrayList<>();
		Map<Pair<String,String>, List<Connection>> connections = getConnections(gtfs);
		for (var entry : connections.entrySet()) {
			
			String from = entry.getKey().getValue0();
			String to = entry.getKey().getValue1();
			Double avgTravelTime = entry.getValue().stream().mapToInt(Connection::getDuration).average().orElse(Double.NaN);
			Integer connectionsPerDay = (int)entry.getValue().size();
			LocalTime firstDepartureTime = entry.getValue().stream().
					map(Connection::getDepartureTime).min(LocalTime::compareTo).orElse(null);
			LocalTime lastDepartureTime = entry.getValue().stream().
					map(Connection::getDepartureTime).max(LocalTime::compareTo).orElse(null);
			
			links.add(new RailLink(from,to,avgTravelTime,connectionsPerDay,firstDepartureTime,lastDepartureTime,"rail"));
		}
		return links;
	}
	
	public static List<RailTransferLink> getTransferLinks(GTFS gtfs){
		List<RailTransferLink> transferLinks = new ArrayList<>();
		List<Transfer> transfers = gtfs.getTransfers();
		
		List<Transfer> filteredTransfers = transfers
				  .stream()
				  .filter(e -> !e.getFromRouteId().equals(e.getToRouteId()))
				  .collect(Collectors.toList());
		
		Map<Pair<String,String>, List<Transfer>> transferMap = filteredTransfers.stream()
				  .collect(Collectors.groupingBy(c -> new Pair<String,String>(c.getFromStopId(), c.getToStopId())));
		
        for (var entry : transferMap.entrySet()) {
			
			String from = entry.getKey().getValue0();
			String to = entry.getKey().getValue1();
			Double avgTravelTime = entry.getValue().stream().mapToInt(Transfer::getMinTransferTime).average().orElse(0.);
			
			transferLinks.add(new RailTransferLink(from,to,avgTravelTime));
		}
		
		return transferLinks;
	}
	

}

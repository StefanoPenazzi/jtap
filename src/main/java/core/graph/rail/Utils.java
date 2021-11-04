package core.graph.rail;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.javatuples.Pair;
import org.neo4j.driver.AccessMode;
import config.Config;
import core.graph.NodeGeoI;
import core.graph.rail.gtfs.GTFS;
import core.graph.rail.gtfs.Stop;
import core.graph.rail.gtfs.StopTime;
import core.graph.rail.gtfs.Transfer;
import data.external.neo4j.Neo4jConnection;

/**
 * @author stefanopenazzi
 *
 */
public final class Utils {
	
	/**
	 * 
	 * @param gtfs
	 * @param database
	 * @param nodes : array containing the nodes types that have to be connected with the GTFS network
	 * @throws Exception
	 */
	public static void insertRailGTFSintoNeo4J(GTFS gtfs,String database,Config config) throws Exception {
		String tempDirectory = config.getGeneralConfig().getTempDirectory();
		data.external.neo4j.Utils.insertNodes(database,tempDirectory,gtfs.getStops());
		data.external.neo4j.Utils.insertLinks(database,tempDirectory,getRailLinks(gtfs)
				,RailLink.class,core.graph.rail.gtfs.Stop.class,"id","stop_from",core.graph.rail.gtfs.Stop.class,"id","stop_to");
		data.external.neo4j.Utils.insertLinks(database,tempDirectory,getRailTransferLinks(gtfs)
				,RailLink.class,core.graph.rail.gtfs.Stop.class,"id","stop_from",core.graph.rail.gtfs.Stop.class,"id","stop_to");
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"CREATE INDEX RailNodeIndex FOR (n:RailNode) ON (n.id)",AccessMode.WRITE);
		}
	}
	
	public static void insertAndConnectRailGTFSIntoNeo4J(GTFS gtfs,String database,Config config,Map<Class<? extends NodeGeoI>,String> nodeArrivalMap) throws Exception {
		insertRailGTFSintoNeo4J(gtfs,database,config);
		core.graph.Utils.setShortestDistCrossLink(database, config.getGeneralConfig().getTempDirectory(),Stop.class,"id", nodeArrivalMap,true);
	}
	
	public static void deleteRailGTFS(String database) throws Exception {
         try( Neo4jConnection conn = new Neo4jConnection()){  
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RailNode)-[r]->(m:RailNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RailNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:RailNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RailNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX RailNodeIndex IF EXISTS",AccessMode.WRITE );
         }
	}
	
	/**
	 * The connections are generated considering the trip. This means that connection
     * between two not consecutive stops are also considered if in the same trip
     * 
	 * a--->b--->c--->d
	 * \        /    /
	 *   ----->     /
	 *   \         /
	 *     ------>
	 *     
	 * If the not direct connections are not intended to be considered, this must 
	 * be specified in the config file 
	 *  
	 * 
	 * @param gtfs
	 * @return
	 */
	public static Map<Pair<String,String>, List<Connection>> getRailConnections(GTFS gtfs, Boolean directConnections){
		List<Connection> connections = new ArrayList<>();
		List<StopTime> stopTime = gtfs.getStopTimes();
		Map<String, List<StopTime>> tripStops = stopTime.stream()
				 .filter(x -> x.getTripId().contains("2021-07-18"))
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
		    if(directConnections) {
		    	for(int j=2;j<st.size()-1;j++) {
			    	Connection c = new Connection(st.get(0).getStopId(),
			    			st.get(j).getStopId(),
			    			st.get(0).getDepartureTime(),
			    			st.get(j).getArrivalTime().toSecondOfDay()-
		                    		  st.get(0).getDepartureTime().toSecondOfDay());
			    	connections.add(c);
			    }
		    }
		}
		Map<Pair<String,String>, List<Connection>> connectionsMap = connections.stream()
				  .collect(Collectors.groupingBy(c -> new Pair<String,String>(c.getFrom(), c.getTo())));
		return connectionsMap;
	}
	
	
	/**
	 * @param gtfs
	 * @return
	 */
	public static List<RailLink> getRailLinks(GTFS gtfs){
		List<RailLink> links = new ArrayList<>();
		Map<Pair<String,String>, List<Connection>> connections = getRailConnections(gtfs,true);
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
	
	/**
	 * @param gtfs
	 * @return
	 */
	public static List<RailTransferLink> getRailTransferLinks(GTFS gtfs){
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

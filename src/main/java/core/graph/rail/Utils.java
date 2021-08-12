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
	}
	
	public static void insertAndConnectRailGTFSIntoNeo4J(GTFS gtfs,String database,Config config,Map<Class<? extends NodeGeoI>,String> nodeArrivalMap) throws Exception {
		insertRailGTFSintoNeo4J(gtfs,database,config);
		core.graph.Utils.setShortestDistCrossLink(database, config.getGeneralConfig().getTempDirectory(),Stop.class,"id", nodeArrivalMap);
	}
	
	public static void deleteRailGTFS(String database) throws Exception {
         try( Neo4jConnection conn = new Neo4jConnection()){  
			//delete from the db all the rail nodes and links
			conn.query(database,"MATCH (n:RailNode) DETACH DELETE n;",AccessMode.WRITE );
         }
	}
	
	/**
	 * @param gtfs
	 * @return
	 */
	public static Map<Pair<String,String>, List<Connection>> getRailConnections(GTFS gtfs){
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
	
	
	/**
	 * @param gtfs
	 * @return
	 */
	public static List<RailLink> getRailLinks(GTFS gtfs){
		List<RailLink> links = new ArrayList<>();
		Map<Pair<String,String>, List<Connection>> connections = getRailConnections(gtfs);
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

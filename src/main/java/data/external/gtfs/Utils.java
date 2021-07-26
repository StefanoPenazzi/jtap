package data.external.gtfs;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.javatuples.Pair;


public final class Utils {
	
	public static void fromGTFStoNEO4J(GTFS gtfs) {
		
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
	
	public static List<Link> getLinks(GTFS gtfs){
		List<Link> links = new ArrayList<>();
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
			
			links.add(new Link(from,to,avgTravelTime,connectionsPerDay,firstDepartureTime,lastDepartureTime));
		}
		return links;
	}

}

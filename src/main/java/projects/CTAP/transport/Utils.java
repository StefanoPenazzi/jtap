package projects.CTAP.transport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Relationship;
import config.Config;
import config.CtapTransportLinkConfig;
import controller.Controller;
import projects.CTAP.graphElements.CTAPTransportLink;

public class Utils {
	
	public static void insertCTAPTransportLinkFactory() throws Exception {
		
		Config config = Controller.getConfig();
		CtapTransportLinkConfig tlc = config.getCtapModelConfig().getTransportConfig().getCtapTransportLinkConfig();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		String query = "match (n)-[r:RoadLink|RailLink|CrossLink]->(m) where (n:CityNode OR n:RailNode OR n:RoadNode) AND (m:CityNode OR m:RailNode OR m:RoadNode) return r";
		//String query = "match (n)-[r:RailLink]-(m) where (n:CityNode OR n:RailNode OR n:RoadNode) AND (m:CityNode OR m:RailNode OR m:RoadNode) return r limit 100";
		
		List<Record> links = data.external.neo4j.Utils.runQuery(query,AccessMode.READ);
		List<CTAPTransportLink> linksList = new ArrayList<>();
		
		for(Record rec: links) {
			Relationship ii = rec.values().get(0).asRelationship();
			double value = 0;
			switch(ii.type()){
				case "CrossLink":
					value = CTAPTransportCrossLinkValue(tlc,ii.asMap());
					break;
				case "RoadLink":
					value = CTAPTransportRoadLinkValue(tlc,ii.asMap());
					break;
				case "RailLink":
					value = CTAPTransportRailLinkValue(tlc,ii.asMap(),dateFormat);
					break;
				default:
					throw new Exception("Link not found");
			}
			linksList.add(new CTAPTransportLink(ii.startNodeId(),ii.endNodeId(),ii.type(),value));
		}	
		
		//delete links if present
		data.external.neo4j.Utils.deleteLinks(CTAPTransportLink.class);
		
		//add the links into the database
    	data.external.neo4j.Utils.insertLinks(linksList,CTAPTransportLink.class,"from_id","to_id");
		
	}
	
	public static double CTAPTransportCrossLinkValue(CtapTransportLinkConfig tlc,Map<String,Object> inputData) {
		return ((Long)inputData.get("distance")).doubleValue() * 1.42 * tlc.getMvTransferTime().getWalk();
	}
	
	public static double CTAPTransportRoadLinkValue(CtapTransportLinkConfig tlc,Map<String,Object> inputData) {
		return Double.parseDouble(inputData.get("avg_travel_time").toString()) *tlc.getMvTransferTime().getCar();
	}
	
	//avg train speed 110km/h => 30.55 m/sec
	//avg waiting time 300sec
	public static double CTAPTransportRailLinkValue(CtapTransportLinkConfig tlc,Map<String,Object> inputData,DateFormat dateFormat) throws ParseException {
		String ldt = (String)inputData.get("last_dep_time");
		String fdt = (String)inputData.get("first_dep_time");
		
		if(ldt == null) {
			ldt = "23:59";
		}
		if(fdt == null) {
			fdt = "00:00";
		}
		
		Date lastDepTime = dateFormat.parse(ldt);
		Date firstDepTime = dateFormat.parse(fdt);
		Long diff = (lastDepTime.getTime() - firstDepTime.getTime())/1000;
		Long connPerDay = (Long)inputData.get("connections_per_day");
		
		if(connPerDay == null) {
			connPerDay = 1L;
		}
		
		double reliability = ((diff.doubleValue()/connPerDay.doubleValue())/2)*tlc.getMvServicePerceivedReliability();
		
		return (double)inputData.get("avg_travel_time")*tlc.getMvTransferTime().getTrain() 
				+ (double)inputData.get("avg_travel_time")*30.55*tlc.getPriceXKm().getTrain()
				+ reliability
				+ 300 * tlc.getMvWaitingTime();
	}
	
}

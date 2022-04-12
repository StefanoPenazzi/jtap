package projects.CTAP.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Relationship;
import config.Config;
import controller.Controller;
import projects.CTAP.graphElements.CTAPTransportLink;

public abstract class AbstractCTAPTransportLinkFactory {
	
    public void insertCTAPTransportLinkFactory(Object tlc) throws Exception {
		
		Config config = Controller.getConfig();
		String query = "match (n)-[r:RoadLink|RailLink|CrossLink|AirLink]->(m) where (n:CityNode OR n:RailNode OR n:RoadNode OR n:AirNode) AND (m:CityNode OR m:RailNode OR m:RoadNode OR n:AirNode) return r";
		
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
					value = CTAPTransportRailLinkValue(tlc,ii.asMap());
					break;
				case "AirLink":
					value = CTAPTransportAirLinkValue(tlc,ii.asMap());
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
	
	public abstract double CTAPTransportCrossLinkValue(Object tlc,Map<String,Object> inputData);
	
	public abstract double CTAPTransportRoadLinkValue(Object tlc,Map<String,Object> inputData);
	
	public abstract double CTAPTransportRailLinkValue(Object tlc,Map<String,Object> inputData);
	
	public abstract double CTAPTransportAirLinkValue(Object tlc,Map<String,Object> inputData);

}

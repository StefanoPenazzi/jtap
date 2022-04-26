package core.graph.air;

import org.neo4j.driver.AccessMode;

import config.Config;
import controller.Controller;
import core.graph.geo.CityNode;
import data.external.neo4j.Neo4jConnection;
import projects.CTAP.graphElements.DestinationProbLink;

public class Utils {
	
	public static void insertAirNetworkNeo4j() throws Exception {
		Config config = Controller.getConfig();
		//delete existent
		deleteAirNetwork();
		
		//insert nodes
		data.external.neo4j.Utils.insertNodes(config.getDbScenarioConfig().getAirConfig().getAirportsDirectory(),AirNode.class);
		
		//index
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(config.getNeo4JConfig().getDatabase(),"CREATE INDEX AirNodeIndex FOR (n:AirNode) ON (n.airport_id)",AccessMode.WRITE);
		}
		
		//insert links
		data.external.neo4j.Utils.insertLinks(config.getDbScenarioConfig().getAirConfig().getConnectionsDirectory(),
				AirLink.class,AirNode.class,"airport_id","id_from",AirNode.class,"airport_id","id_to");
	}

	public static void deleteAirNetwork() throws Exception {
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
         try( Neo4jConnection conn = new Neo4jConnection()){  
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RoadNode)-[r:CrossLink|CTAPTransportLink]->(m:AirNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RoadNode)<-[r:CrossLink|CTAPTransportLink]-(m:AirNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RailNode)-[r:CrossLink|CTAPTransportLink]->(m:AirNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:RailNode)<-[r:CrossLink|CTAPTransportLink]-(m:AirNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:AirNode)-[r:AirLink|CTAPTransportLink]-(m:AirNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:AirNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX AirNodeIndex IF EXISTS",AccessMode.WRITE );
         }
	}
	
}

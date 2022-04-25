package projects.CTAP.activityLocationSequence;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.AccessMode;
import config.Config;
import controller.Controller;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import data.external.neo4j.Neo4jConnection;
import projects.CTAP.graphElements.DestinationProbLink;

public class Utils {
	
	public static void insertDestinationProbIntoNeo4j() throws Exception {
		Config config = Controller.getConfig();
		
		//delete existent links
		deleteDestinationProbLinks();
    	
		data.external.neo4j.Utils.insertLinks(config.getCtapModelConfig().getCtapActivityLocationConfig().getDestinationsProbDistFile(),
					DestinationProbLink.class,CityNode.class,"city_id","from_id",CityNode.class,"city_id","to_id");
		
		
	}
	
	public static void deleteDestinationProbLinks() throws Exception {
		try( Neo4jConnection conn = Controller.getInjector().getInstance(Neo4jConnection.class)){  
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r:DestinationProbLink]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
		}
	}

}

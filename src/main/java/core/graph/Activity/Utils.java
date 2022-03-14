package core.graph.Activity;

import org.neo4j.driver.AccessMode;

import config.Config;
import controller.Controller;
import core.graph.geo.CityNode;
import core.graph.population.AgentActivityLink;
import core.graph.population.AgentFamilyLink;
import core.graph.population.AgentGeoLink;
import core.graph.population.StdAgentNodeImpl;
import data.external.neo4j.Neo4jConnection;

public class Utils {
	
	public static void insertActivitiesFromCsv(Class<ActivityNode> sai) throws Exception {
		//insert nodes 
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		core.graph.Utils.insertNodesIntoNeo4J(database,config.getDbScenarioConfig().getActivitiesConfig().getActivitiesFile(),config.getGeneralConfig().getTempDirectory(),sai);
		data.external.neo4j.Utils.createIndex(database,"ActivityNodeIndex","ActivityNode","activity_id");
	}
	
	public static void insertActivitiesLocFromCsv(Class<? extends ActivityLocLink> sai) throws Exception {
		//insert nodes 
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,data.external.neo4j.Utils.getLoadCSVLinkQuery(config.getDbScenarioConfig().getActivitiesConfig().getActivitiesLocFile(),
					sai,ActivityNode.class,"activity_id","activity_id",CityNode.class,"city_id","city_id"),AccessMode.WRITE );
		}
	}
	
	public static void deleteActivities(Config config) throws Exception {
		String database = config.getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:ActivityNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX ActivityNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 
	
	public static void deleteActivities() throws Exception {
		try(Neo4jConnection conn = Controller.getNeo4JConnection()){  
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:ActivityNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode)-[r:ActivityLocLink]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query("DROP INDEX ActivityNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 

}

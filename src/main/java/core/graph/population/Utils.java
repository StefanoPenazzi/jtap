package core.graph.population;

import org.neo4j.driver.AccessMode;
import config.Config;
import controller.Controller;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import data.external.neo4j.Neo4jConnection;


public class Utils {
	
	public static void insertStdPopulationFromCsv(Class<StdAgentImpl> sai) throws Exception {
		//insert nodes 
		Config config = Controller.getConfig();
		String database = config.getNeo4JConfig().getDatabase();
		core.graph.Utils.insertNodesIntoNeo4J(database,config.getPopulationConfig().getAgentFile(),config.getGeneralConfig().getTempDirectory(),sai);
		data.external.neo4j.Utils.createIndex(database,"AgentNodeIndex","AgentNode","agent_id");
		
		try( Neo4jConnection conn = new Neo4jConnection()){  
			//city
			conn.query(database,data.external.neo4j.Utils.getLoadCSVLinkQuery(config.getPopulationConfig().getResidenceFile(),
					AgentGeoLink.class,StdAgentImpl.class,"agent_id","agent_id",CityNode.class,"city","city"),AccessMode.WRITE );
			//families
			conn.query(database,data.external.neo4j.Utils.getLoadCSVLinkQuery(config.getPopulationConfig().getFamilyFile(),
					AgentFamilyLink.class,StdAgentImpl.class,"agent_id","agent_from_id",StdAgentImpl.class,"agent_id","agent_to_id"),AccessMode.WRITE );
			//friends
			conn.query(database,data.external.neo4j.Utils.getLoadCSVLinkQuery(config.getPopulationConfig().getFriendFile(),
					AgentFamilyLink.class,StdAgentImpl.class,"agent_id","agent_from_id",StdAgentImpl.class,"agent_id","agent_to_id"),AccessMode.WRITE );
			//activities
			conn.query(database,data.external.neo4j.Utils.getLoadCSVLinkQuery(config.getPopulationConfig().getActivityFile(),
					AgentActivityLink.class,StdAgentImpl.class,"agent_id","agent_id",ActivityNode.class,"activity_id","activity_id"),AccessMode.WRITE );
		}
	}
	
	public static void deletePopulation(Config config) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){ 
			String database = config.getNeo4JConfig().getDatabase();
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:AgentNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:AgentNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:AgentNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX AgentNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 
	
	public static void deletePopulation() throws Exception {
		try(Neo4jConnection conn = Controller.getNeo4JConnection()){ 
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:AgentNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:AgentNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:AgentNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query("DROP INDEX AgentNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 
}

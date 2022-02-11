package core.graph.Activity;

import org.neo4j.driver.AccessMode;

import config.Config;
import data.external.neo4j.Neo4jConnection;

public class Utils {
	
	public static void insertActivitiesFromCsv(String database,Config config,Class<ActivityNode> sai) throws Exception {
		//insert nodes 
		core.graph.Utils.insertNodesIntoNeo4J(database,config.getActivitiesConfig().getActivitiesFile(),config.getGeneralConfig().getTempDirectory(),sai);
		data.external.neo4j.Utils.createIndex(database,"ActivityNodeIndex","ActivityNode","activity_id");
		
	}
	
	public static void deleteActivities(String database) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode)-[r]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
        	conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r]->(m:ActivityNode) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n:ActivityNode) RETURN n limit 10000000\", \"delete n\",{batchSize:100000});",AccessMode.WRITE );
			conn.query(database,"DROP INDEX ActivityNodeIndex IF EXISTS",AccessMode.WRITE );
		}
	} 

}

package projects.CTAP.attractiveness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import config.Config;
import controller.Controller;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import data.external.neo4j.Neo4jConnection;


public class Utils {
	
	public static void attractivenessNormalized(Integer popThreshold, Integer initialTime, Integer finalTime, Integer timeInterval, AttractivenessCTAP actp) throws Exception {
		
		Config config = Controller.getConfig();
		AttractivenessCTAP an = Controller.getInjector().getInstance(AttractivenessCTAP .class);
		Integer intervals = (int) Math.ceil((finalTime-initialTime)/timeInterval);
		//get all the cities stat nodes
		String query = "match (m:CityFacStatNode)-[r]-(n:CityNode) where n.population >= "+popThreshold+" return m";
    	List<Record> cityFacStatNodeRecords = data.external.neo4j.Utils.runQuery(query,AccessMode.READ);
    	List<StdAgentNodeImpl> agents = data.external.neo4j.Utils.importNodes(StdAgentNodeImpl.class);
    	List<ActivityNode> activities = data.external.neo4j.Utils.importNodes(ActivityNode.class);
    	
    	//calculate the attractivness for each agent-city-time
    	List<AttractivenessCTAPLink> attractivenessList = new ArrayList<>();
    	for(StdAgentNodeImpl agentNode: agents) {
    		for(ActivityNode activityNode: activities) {
    			for(Record rec: cityFacStatNodeRecords) {
    				
    				Double[] variables = new Double[3];
    				Map<String,Object> cityStats = rec.values().get(0).asMap();
    				maskMap(cityStats,variables);
    				
	        		for(int j = 0;j<intervals;j++) {
	        			Double time = new Double(timeInterval*j);
	        			variables[2] = time;
	        			attractivenessList.add(new AttractivenessCTAPLink(agentNode.getId(),
	        					(String)cityStats.get("city"),
	        					activityNode.getActivityId(),
	        					time,
	        					actp.getAttractiveness(variables,1,activityNode.getActivityName())));
	        		}
    			}
        	}
    	}
    	
    	//normalization
    	
    	//add the links in neo4j
    	data.external.neo4j.Utils.insertLinks(attractivenessList,AttractivenessCTAPLink.class,StdAgentNodeImpl.class,"agent_id",CityNode.class,"city");
    
		System.out.println();
	}
	
	
	
	/**
	 * @param map
	 * @param variables
	 */
	public static void maskMap(Map<String,Object> map, Double[] variables){
		variables[0] =((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue();
		variables[1] = ((map.get("restaurant") == null? new Long(0): (Long)map.get("restaurant"))).doubleValue();
		IntStream.range(0, variables.length).forEach(x -> {
			if(variables[x] == null) {
				variables[x] = 0d; 
			}
		});       
	}
	
	public static void deleteAttractivenessLinks() throws Exception {
		try( Neo4jConnection conn = Controller.getInjector().getInstance(Neo4jConnection.class)){  
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r:AttractivenessCTAPLink]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
		}
	}

}

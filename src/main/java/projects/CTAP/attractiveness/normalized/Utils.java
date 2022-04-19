package projects.CTAP.attractiveness.normalized;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import config.AttractivenessNormalizedConfig;
import config.Config;
import controller.Controller;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import data.external.neo4j.Neo4jConnection;
import projects.CTAP.attractiveness.AttractivenessModelI;
import projects.CTAP.attractiveness.AttractivenessModelVariablesI;
import projects.CTAP.graphElements.AttractivenessNormalizedLink;


public class Utils {
	
	/**
	 * @param popThreshold
	 * @param initialTime
	 * @param finalTime
	 * @param timeInterval
	 * @param actp
	 * @throws Exception
	 */
	public static void insertAttractivenessNormalizedIntoNeo4j(AttractivenessModelI attractivenessModel, AttractivenessModelVariablesI variables) throws Exception {
		
		Config config = Controller.getConfig();
		AttractivenessNormalizedConfig anc = config.getCtapModelConfig().getAttractivenessModelConfig().getAttractivenessNormalizedConfig();
		DefaultAttractivenessModelImpl an = (DefaultAttractivenessModelImpl)Controller.getInjector().getInstance(AttractivenessModelI.class);
		
		Integer popThreshold = config.getCtapModelConfig().getDatasetConfig().getNewDatasetParams().getDestinationsPopThreshold();
		Integer initialTime = (int)anc.getInitialTime();
		Integer finalTime = (int)anc.getFinalTime();
		Integer timeInterval = (int)anc.getIntervalTime();
		
		Integer intervals = (int) Math.ceil((finalTime-initialTime)/timeInterval);
		//cities stat nodes
		String query = "match (m:CityFacStatNode)-[r]-(n:CityNode) where n.population >= "+popThreshold+" return m,n";
    	List<Record> cityFacStatNodeRecords = data.external.neo4j.Utils.runQuery(query,AccessMode.READ);
    	//agents
    	List<StdAgentNodeImpl> agents = data.external.neo4j.Utils.importNodes(StdAgentNodeImpl.class);
    	//activities
    	List<ActivityNode> activities = data.external.neo4j.Utils.importNodes(ActivityNode.class);
    	//calculate the attractivness for each agent-city-time
    	List<AttractivenessNormalizedLink> attractivenessList = new ArrayList<>();
    	for(StdAgentNodeImpl agentNode: agents) {
    		for(ActivityNode activityNode: activities) {
    			for(Record rec: cityFacStatNodeRecords) {
    				Map<String,Object> cityStats = rec.values().get(0).asMap();
    				Long cityId = rec.values().get(1).get("city_id").asLong();
    				List<Double> vars = variables.getVariables(cityStats);
    				vars.add(0d);
	        		for(int j = 0;j<intervals;j++) {
	        			Double time = new Double(timeInterval*j);
	        			vars.set(vars.size()-1, time);
	        			attractivenessList.add(new AttractivenessNormalizedLink(agentNode.getId(),
	        					cityId,
	        					activityNode.getActivityId(),
	        					time,
	        					attractivenessModel.getAttractiveness(vars.toArray(new Double[vars.size()]),1,activityNode.getActivityName())));
	        		}
    			}
        	}
    	}
    	
    	//normalization 
    	Map<Long, Map<Long, Optional<AttractivenessNormalizedLink>>> normal = attractivenessList.stream()
    			.collect(Collectors.groupingBy(AttractivenessNormalizedLink::getAgentId,
    			Collectors.groupingBy(AttractivenessNormalizedLink::getActivityId, Collectors.maxBy(Comparator.comparing(AttractivenessNormalizedLink::getAttractiveness)))));
    	
    	Map<Long, Map<Long, Double>> normal_ = new HashMap<>();
    	for( Map.Entry<Long, Map<Long, Optional<AttractivenessNormalizedLink>>> entry : normal.entrySet() ) {
    		normal_.put(entry.getKey(),new HashMap<Long, Double>());
    		for(Map.Entry<Long, Optional<AttractivenessNormalizedLink>> entry_1 : entry.getValue().entrySet()) {
    			normal_.get(entry.getKey()).put(entry_1.getKey(),entry_1.getValue().orElseThrow(() -> new RuntimeException("Missing max AttractivenessCTAPLink")).getAttractiveness());
    		}
    	}
    			
    	for(AttractivenessNormalizedLink actpl : attractivenessList) {
    		Double max_ = normal_.get(actpl.getAgentId()).get(actpl.getActivityId());
    		actpl.setAttractiveness(actpl.getAttractiveness()/max_);
    	}
    	
    	//delete existent links
    	deleteAttractivenessLinks();
    	
    	//add the links into the database
    	data.external.neo4j.Utils.insertLinks(attractivenessList,AttractivenessNormalizedLink.class,StdAgentNodeImpl.class,"agent_id",CityNode.class,"city_id");
    
	}
	
	/**
	 * @throws Exception
	 */
	public static void deleteAttractivenessLinks() throws Exception {
		try( Neo4jConnection conn = Controller.getInjector().getInstance(Neo4jConnection.class)){  
			conn.query("Call apoc.periodic.iterate(\"cypher runtime=slotted Match (n)-[r:AttractivenessNormalizedLink]->(m) RETURN r limit 10000000\", \"delete r\",{batchSize:100000});",AccessMode.WRITE );
		}
	}

}

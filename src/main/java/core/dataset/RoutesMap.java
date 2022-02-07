package core.dataset;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import config.Config;
import core.graph.NodeGeoI;
import core.graph.routing.RoutingGraph;
import core.graph.routing.RoutingManager;


public class RoutesMap implements DatasetMapI {
	
	private Map<String,Map<String,Map<String,Double>>> map  = new HashMap<>();
	private List<RoutingGraph> projections = new ArrayList<>();
	private RoutingManager rm;
	private Config config;
	
	@Inject 
	public RoutesMap(Config config,RoutingManager rm){
		this.config = config;
		this.rm = rm;
	}
	
	
	public void addProjection(RoutingGraph projection) throws Exception {
		this.rm.addNewRoutingGraph(projection);
		map.put(projection.getId(), new HashMap<>());
	    this.projections.add(projection);
	}
	
	public void addProjections(List<RoutingGraph> projections_) throws Exception {
		for (RoutingGraph rg: projections_) {
			this.rm.addNewRoutingGraph(rg);
			map.put(rg.getId(), new HashMap<>());
		}
		this.projections.addAll(projections_);
	}
	
	
	public void addSourceTargetRoutesFromNeo4j(List<SourceTargetRouteRequest> str ) {
		
	}
	
    public void addSourceRoutesFromNeo4j(List<SourceRoutesRequest> srr ) throws Exception {
    	for(SourceRoutesRequest req: srr) {
    		Map<String,Double> res = rm.getSSSP_AsMap(req.getRg(),
    				req.getSource(),
    				req.getSourcePropertyKey(),
    				req.getSourcePropertyValue(),
    				req.targetPropertyKey,
    				req.getWeightProperty());
    		Map<String,Map<String,Double>> m = map.get(req.getRg());
    		if(m.containsKey(req.getSourcePropertyValue())) {
    			Map<String, Double> map3 = Stream.of(res, m.get(req.getSourcePropertyValue()))
    					  .flatMap(map -> map.entrySet().stream())
    					  .collect(Collectors.toMap(
    					    Map.Entry::getKey,
    					    Map.Entry::getValue));
    		}
    		else {
    			m.put(req.getSourcePropertyValue(),res);
    		}
    	}
	}
    public void addNewRoutesFromCSV() {
		
	}
    public void addNewRoutesFromJson() throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	FileInputStream inputStream = new FileInputStream(config.getRoutingConfig().getJsonFile());
    	try {
    	    String json = IOUtils.toString(inputStream);
    	    this.map = mapper.readValue(json, Map.class);
    	    System.out.println();
    	    
    	} finally {
    	    inputStream.close();
    	}
   	}
    
    public void saveCSV() {
    	String eol = System.getProperty("line.separator");
    	try (Writer writer = new FileWriter(config.getGeneralConfig().getOutputDirectory()+"RoutesMap.csv")) {
    	  for (Map.Entry<String,Map<String,Map<String,Double>>> entry : this.map.entrySet()) {
    	    for (Map.Entry<String,Map<String,Double>> entry_1 : entry.getValue().entrySet()) {
    	    	for (Map.Entry<String,Double> entry_2 : entry_1.getValue().entrySet()) {
    	    		 writer.append(entry.getKey()).append(',');
    	    		 writer.append(entry_1.getKey()).append(',');
        	    	 writer.append(entry_2.getKey()).append(',');
        	    	 writer.append(entry_2.getValue().toString())
        	    	 .append(eol);
    	    	}
    	    }
    	  }
    	} catch (IOException ex) {
    	  ex.printStackTrace(System.err);
    	}
    }
    
    public void saveJson() {
    	ObjectMapper mapper = new ObjectMapper();
    	try (Writer writer = new FileWriter(config.getGeneralConfig().getOutputDirectory()+"RoutesMap.txt")) {
    	     writer.append(mapper.writeValueAsString(map));
    	} catch (IOException ex) {
    	  ex.printStackTrace(System.err);
    	}
    }
    
    public void close() throws Exception {
    	this.rm.close();
    }
	
    class SourceTargetRouteRequest<T extends NodeGeoI,K extends NodeGeoI>{
    	String rg;
    	T source;
    	K target;
    	String sourcePropertyKey;
    	String sourcePropertyValue;
		String targetPropertyKey;
		String targetPropertyValue;
		String weightProperty;
    	public SourceTargetRouteRequest(String rg, T source, K target, String startPropertyKey, String startPropertyValue,
   			 String endPropertyKey, String endPropertyValue, String weightProperty) {
    		
    		this.rg = rg;
    		this.source = source;
    		this.target = target;
    		this.sourcePropertyKey = startPropertyKey;
    		this.sourcePropertyValue = startPropertyValue;
    		this.targetPropertyKey = endPropertyKey;
    		this.targetPropertyValue = endPropertyValue;
    		this.weightProperty = weightProperty;
    	}
    	
    	public String getRg() {
    		return this.rg;
    	}
    	public T getSource() {
    		return this.source;
    	}
    	public K getTarget() {
    		return this.target;
    	}
    	public String getSourcePropertyKey() {
    		return this.sourcePropertyKey;
    	}
    	public String getSourcePropertyValue() {
    		return this.sourcePropertyValue;
    	}
    	public String getTargetPropertyKey() {
    		return this.targetPropertyKey;
    	}
    	public String getTargetPropertyValue() {
    		return this.targetPropertyValue;
    	}
    	public String getWeightProperty() {
    		return this.weightProperty;
    	}
    	
    }
    class SourceRoutesRequest<T extends NodeGeoI>{
    	private String rg;
    	private T source;
    	private String sourcePropertyKey;
    	private String sourcePropertyValue;
    	private String targetPropertyKey;
    	private String weightProperty;
    	public SourceRoutesRequest(String rg, T source, String startPropertyKey, String startPropertyValue,
   			 String endPropertyKey, String weightProperty) {
    		
    		this.rg = rg;
    		this.source = source;
    		this.sourcePropertyKey = startPropertyKey;
    		this.sourcePropertyValue = startPropertyValue;
    		this.targetPropertyKey = endPropertyKey;
    		this.weightProperty = weightProperty;
    		
    	}
    	
    	public String getRg() {
    		return this.rg;
    	}
    	public T getSource() {
    		return this.source;
    	}
    	public String getSourcePropertyKey() {
    		return this.sourcePropertyKey;
    	}
    	public String getSourcePropertyValue() {
    		return this.sourcePropertyValue;
    	}
    	public String getTargetPropertyKey() {
    		return this.targetPropertyKey;
    	}
    	public String getWeightProperty() {
    		return this.weightProperty;
    	}
    	
    }
	
    
    
}

package core.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.graph.NodeGeoI;
import core.graph.routing.RoutingGraph;
import core.graph.routing.RoutingManager;

public class RoutesMap {
	
	private Map<String,Map<String,Map<String,Double>>> map;
	private List<RoutingGraph> projections;
	private RoutingManager rm = new RoutingManager();
	
	public RoutesMap(List<RoutingGraph> projections) throws Exception {
		this.projections = projections;
		this.map = new HashMap<>();
		for (RoutingGraph rg: projections) {
			this.rm.addNewRoutingGraph(rg);
			map.put(rg.getId(), new HashMap<>());
		}
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
    
    public void save() {
    	
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

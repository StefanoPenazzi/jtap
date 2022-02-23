package core.dataset;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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
	
	private Map<String,Map<String,Map<String,Double>>> map  = new ConcurrentHashMap<>();
	private List<RoutingGraph> projections = new ArrayList<>();
	private RoutingManager rm;
	private Config config;
	public static final String ROUTES_MAP_KEY = "RoutesMap";
	
	public RoutesMap(Config config,RoutingManager rm){
		this.config = config;
		this.rm = rm;
	}
	
	
	public void addProjection(RoutingGraph projection) throws Exception {
		this.rm.addNewRoutingGraph(projection);
		map.put(projection.getId(), new ConcurrentHashMap<>());
	    this.projections.add(projection);
	}
	
	public void addProjections(List<RoutingGraph> projections_) throws Exception {
		for (RoutingGraph rg: projections_) {
			this.rm.addNewRoutingGraph(rg);
			map.put(rg.getId(), new ConcurrentHashMap<>());
		}
		this.projections.addAll(projections_);
	}
	
	
	public void addSourceTargetRoutesFromNeo4j(List<SourceTargetRouteRequest> str ) {
		
	}
	
    /**
     * @param srr
     * @throws Exception
     * 
     * In case a SourceRoutesRequest has the List<String> targets = null all the targets are considered
     */
    public void addSourceRoutesFromNeo4j(List<SourceRoutesRequest> srr ) throws Exception {
    	for(@SuppressWarnings("rawtypes") SourceRoutesRequest req: srr) {
    		Map<String,Double> res = rm.getSSSP_AsMap(req.getRg(),
    				req.getSource(),
    				req.getSourcePropertyKey(),
    				req.getSourcePropertyValue(),
    				req.targetPropertyKey,
    				req.getWeightProperty());
    		//filter the targets
    		if(req.getTargets() != null) {
    			List<String> ls = (List<String>)req.getTargets();
    			res.keySet().retainAll(ls);
    		}
    		Map<String,Map<String,Double>> m = map.get(req.getRg());
    		if(m.containsKey(req.getSourcePropertyValue())) {
    			Map<String, Double> map3 = Stream.of(res, m.get(req.getSourcePropertyValue()))
    					  .flatMap(map -> map.entrySet().stream())
    					  .collect(Collectors.toConcurrentMap(
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
		
    	/**
    	 * @param rg
    	 * @param source
    	 * @param target
    	 * @param startPropertyKey
    	 * @param startPropertyValue
    	 * @param endPropertyKey
    	 * @param endPropertyValue
    	 * @param weightProperty
    	 */
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
    
    
    /**
     * @author stefanopenazzi
     *
     * @param <T>
     */
    public class SourceRoutesRequest<T extends NodeGeoI>{
    	private final String rg;
    	private final T source;
    	private final String sourcePropertyKey;
    	private final String sourcePropertyValue;
    	private final String targetPropertyKey;
    	private final String weightProperty;
    	private final List<String> targets;
    	/**
    	 * @param rg
    	 * @param source
    	 * @param startPropertyKey
    	 * @param startPropertyValue
    	 * @param endPropertyKey
    	 * @param weightProperty
    	 * @param targets            if this is null all the targets are considered
    	 */
    	public SourceRoutesRequest(String rg, T source, String startPropertyKey, String startPropertyValue,
   			 String endPropertyKey, String weightProperty,List<String> targets) {
    		
    		this.rg = rg;
    		this.source = source;
    		this.sourcePropertyKey = startPropertyKey;
    		this.sourcePropertyValue = startPropertyValue;
    		this.targetPropertyKey = endPropertyKey;
    		this.weightProperty = weightProperty;
    		this.targets = targets;
    		
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
    	public List<String> getTargets(){
    		return this.targets;
    	}
    	
    }
    
    
	@Override
	public String getKey() {
		return ROUTES_MAP_KEY;
	}


	@Override
	public void initialization() {
		
	}
	
    
    
}

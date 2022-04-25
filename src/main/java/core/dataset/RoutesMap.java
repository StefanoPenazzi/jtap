package core.dataset;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import core.graph.NodeGeoI;
import core.graph.routing.PathRes;
import core.graph.routing.RoutingGraph;
import core.graph.routing.RoutingManager;



public class RoutesMap implements DatasetMapI {
	
	private Long projectionsCount = 0L;
	private Map<String,Long> projectionsMap = new HashMap<>();
	private Map<Long,Map<Long,Map<Long,PathRes>>> map  = new ConcurrentHashMap<>();
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
		projectionsMap.put(projection.getId(), projectionsCount);
		map.put(projectionsCount, new ConcurrentHashMap<>());
	    this.projections.add(projection);
	    projectionsCount++;
	}
	
	public void addProjections(List<RoutingGraph> projections_) throws Exception {
		for (RoutingGraph rg: projections_) {
			this.rm.addNewRoutingGraph(rg);
			projectionsMap.put(rg.getId(), projectionsCount);
			map.put(projectionsCount, new ConcurrentHashMap<>());
			projectionsCount++;
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
    		Map<Long,PathRes> res = rm.getSSSP_AsMap(req.getRg(),
    				req.getSourceType(),
    				"city_id",
    				req.getSourceId(),
    				"city_id",
    				req.getWeightProperty());
    		//filter the targets
    		if(req.getTargetsIds() != null) {
    			List<Long> ls = (List<Long>)req.getTargetsIds();
    			res.keySet().retainAll(ls);
    			for(Long l:ls) {
    				if(!res.containsKey(l)) {
    					PathRes pr = new PathRes(-1,null);
    					res.put(l,pr);
    				}
    			}
    		}
    		Map<Long,Map<Long,PathRes>> m = map.get(projectionsMap.get(req.getRg()));
    		if(m.containsKey(req.getSourceId())) {
    			Map<Long, PathRes> map3 = Stream.of(res, m.get(req.getSourceId()))
    					  .flatMap(map -> map.entrySet().stream())
    					  .collect(Collectors.toConcurrentMap(
    					    Map.Entry::getKey,
    					    Map.Entry::getValue));
    		}
    		else {
    			m.put(req.getSourceId(),res);
    		}
    	}
	}
    
    public void addSourceRoutesWithPathsFromNeo4j(List<SourceRoutesRequest> srr ) throws Exception {
    	for(@SuppressWarnings("rawtypes") SourceRoutesRequest req: srr) {
    		Map<Long,PathRes> res_ = rm.getSSSP_AsMapWithPaths(req.getRg(),
    				req.getSourceType(),
    				"city_id",
    				req.getSourceId(),
    				"city_id",
    				req.getWeightProperty());
    		
    		Map<Long,PathRes> res = new HashMap<>();
    		//filter the targets
    		if(req.getTargetsIds() != null) {
    			List<Long> ls = (List<Long>)req.getTargetsIds();
    			for(Long l:ls) {
    				if(!res_.containsKey(l)) {
    					PathRes pr = new PathRes(-1,null);
    					res.put(l,pr);
    				}
    				else {
    					PathRes pr = (PathRes)res_.get(l).clone();
    					res.put(l,pr);
    				}
    			}
    		}
    		res_ = null;
    		Map<Long,Map<Long,PathRes>> m = map.get(projectionsMap.get(req.getRg()));
    		if(m.containsKey(req.getSourceId())) {
    			Map<Long, PathRes> map3 = Stream.of(res, m.get(req.getSourceId()))
    					  .flatMap(map -> map.entrySet().stream())
    					  .collect(Collectors.toConcurrentMap(
    					    Map.Entry::getKey,
    					    Map.Entry::getValue));
    		}
    		else {
    			m.put(req.getSourceId(),res);
    		}
    	}
	}
    
    public void saveJson() {
    	ObjectMapper mapper = new ObjectMapper();
    	try (Writer writer = new FileWriter(config.getGeneralConfig().getOutputDirectory()+"RoutesMap.json")) {
    	     writer.append(mapper.writeValueAsString(map));
    	} catch (IOException ex) {
    	  ex.printStackTrace(System.err);
    	}
    }
    
    public void close() throws Exception {
    	//this.rm.close();
    }
    
    public double[][][] toArrayCost(List<List<Long>> parameterDescription){
    	
    	int d1 = parameterDescription.get(0).size();
    	int d2 = parameterDescription.get(1).size();
    	int d3 = parameterDescription.get(2).size();
    	
    	double[][][] res = new double[d1][d2][d3];
    	for(int i =0;i < d1;i++) {
    		for(int j = 0;j < d2;j++) {
    			for(int k = 0;k < d3;k++) {
    				res[i][j][k] = map.get(parameterDescription.get(0).get(i))
    						.get(parameterDescription.get(1).get(j))
    						.get(parameterDescription.get(2).get(k)).getTotalCost();
    			}
    		}
    	}
    	return res;
    }
    
    public List<Long>[][][] toArrayPath(List<List<Long>> parameterDescription){
    	
    	int d1 = parameterDescription.get(0).size();
    	int d2 = parameterDescription.get(1).size();
    	int d3 = parameterDescription.get(2).size();
    	
    	List<Long>[][][] res = new ArrayList[d1][d2][d3];
    	for(int i =0;i < d1;i++) {
    		for(int j = 0;j < d2;j++) {
    			for(int k = 0;k < d3;k++) {
    				res[i][j][k] = map.get(parameterDescription.get(0).get(i))
    						.get(parameterDescription.get(1).get(j))
    						.get(parameterDescription.get(2).get(k)).getPath();
    			}
    		}
    	}
    	return res;
    }
	
    
    //TODO switch to LONG
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
    	private final T sourceType;
    	private final Long sourceId;
    	private final String weightProperty;
    	private final List<Long> targetsIds;
    	/**
    	 * @param rg
    	 * @param source
    	 * @param startPropertyKey
    	 * @param startPropertyValue
    	 * @param endPropertyKey
    	 * @param weightProperty
    	 * @param targets            if this is null all the targets are considered
    	 */
    	public SourceRoutesRequest(String rg, T sourceType, Long sourceId,
    			String weightProperty,List<Long> targetsIds) {
    		
    		this.rg = rg;
    		this.sourceType = sourceType;
    		this.sourceId = sourceId;
    		this.weightProperty = weightProperty;
    		this.targetsIds = targetsIds;
    		
    	}
    	
    	public String getRg() {
    		return this.rg;
    	}
    	public T getSourceType() {
    		return this.sourceType;
    	}
    	public Long getSourceId() {
    		return this.sourceId;
    	}
    	public String getWeightProperty() {
    		return this.weightProperty;
    	}
    	public List<Long> getTargetsIds(){
    		return this.targetsIds;
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

package projects.CTAP.geolocClusters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import config.Config;
import controller.Controller;
import core.dataset.RoutesMap;
import core.dataset.RoutesMap.SourceRoutesRequest;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;

public class Utils {
	
	public static List<SourceRoutesRequest> getSRR_cluster1(RoutesMap rm ,String projectionId, Integer popThreshold) throws Exception{
		
		List<SourceRoutesRequest> srr = new ArrayList<>();
		Config config = Controller.getConfig();
		List<CityNode> locations = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),CityNode.class);
		
		List<Long> citiesThresholdUp = locations.stream()
				.filter(e -> e.getPopulation() >= popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		List<Long> citiesThresholdDown = locations.stream()
				.filter(e -> e.getPopulation() < popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		CityNode cityNode = new CityNode();
		
		citiesThresholdUp.forEach(city ->{
			srr.add(rm.new SourceRoutesRequest(projectionId,cityNode,city,"avg_travel_time",null));
		});
		
		citiesThresholdDown.forEach(city ->{
			srr.add(rm.new SourceRoutesRequest(projectionId,cityNode,city,"avg_travel_time",citiesThresholdUp));
		});
		
		return srr;
	}
	
	
    public static List<SourceRoutesRequest> getSRR_clusterDs2Os(RoutesMap rm ,String projectionId, Integer popThreshold) throws Exception{
		
		List<SourceRoutesRequest> srr = new ArrayList<>();
		Config config = Controller.getConfig();
		List<CityNode> locations = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),CityNode.class);
		
		List<Long> citiesThresholdUp = locations.stream()
				.filter(e -> e.getPopulation() >= popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		List<Long> citiesThresholdDown = locations.stream()
				.filter(e -> e.getPopulation() < popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		CityNode cityNode = new CityNode();
		citiesThresholdUp.forEach(city ->{
			srr.add(rm.new SourceRoutesRequest(projectionId,cityNode,city,"avg_travel_time",citiesThresholdDown));
		});
		return srr;
	}
    
    public static List<SourceRoutesRequest> getSRR_clusterOs2Ds(RoutesMap rm ,String projectionId, Integer popThreshold) throws Exception{
		
    	List<SourceRoutesRequest> srr = new ArrayList<>();
		Config config = Controller.getConfig();
		List<CityNode> locations = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),CityNode.class);
		
		List<Long> citiesThresholdUp = locations.stream()
				.filter(e -> e.getPopulation() >= popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		List<Long> citiesThresholdDown = locations.stream()
				.filter(e -> e.getPopulation() < popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		CityNode cityNode = new CityNode();
		citiesThresholdDown.forEach(city ->{
			srr.add(rm.new SourceRoutesRequest(projectionId,cityNode,city,"avg_travel_time",citiesThresholdUp));
		});
		
		return srr;
	}
    
    public static List<SourceRoutesRequest> getSRR_clusterDs2Ds(RoutesMap rm ,String projectionId, Integer popThreshold) throws Exception{
		
    	List<SourceRoutesRequest> srr = new ArrayList<>();
		Config config = Controller.getConfig();
		List<CityNode> locations = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),CityNode.class);
		
		List<Long> citiesThresholdUp = locations.stream()
				.filter(e -> e.getPopulation() >= popThreshold)
				.map(CityNode::getId).collect(Collectors.toList());
		
		CityNode cityNode = new CityNode();
		citiesThresholdUp.forEach(city ->{
			srr.add(rm.new SourceRoutesRequest(projectionId,cityNode,city,"avg_travel_time",citiesThresholdUp));
		});
		
		return srr;
	}

}

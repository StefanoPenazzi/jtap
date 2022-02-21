package core.dataset;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.dataset.RoutesMap.SourceRoutesRequest;
import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;
import core.graph.routing.RoutingGraph;
import projects.CTAP.dataset.RoutesMapCTAP;

import java.nio.file.Path;
import java.nio.file.Paths;

class datasetTest {

	@Test
	void RoutesTest() throws Exception {
		
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		String db = "france2";
		
		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
		List<Class<? extends LinkI>> links = new ArrayList<>();
		nodes.add(CityNode.class);
		nodes.add(RoadNode.class);
		links.add(CrossLink.class);
		links.add(RoadLink.class);
		
		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
		
		List<RoutingGraph> rgs = new ArrayList<RoutingGraph>();
		rgs.add(rg);
		
		RoutesMap rm = controller.getInjector().getInstance(RoutesMap.class);
		rm.addProjections(rgs);
		CityNode cityNode = new CityNode();
		List<SourceRoutesRequest> srr = new ArrayList<>();
		//srr.add(rm.new SourceRoutesRequest("train-intersections-graph-2",city,"city","Courcouronnes","city","avg_travel_time"));
		srr.add(rm.new SourceRoutesRequest("train-intersections-graph-2",cityNode,"city","Paris","city","avg_travel_time",null));
		//rm.addSourceRoutesFromNeo4j(srr);
		rm.addNewRoutesFromJson();
		//rm.saveJson();
		System.out.println();
		rm.close();
	}
	
	@Test
	void RoutesClustersTest() throws Exception {
		
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		String db = "france2";
		
		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
		List<Class<? extends LinkI>> links = new ArrayList<>();
		nodes.add(CityNode.class);
		nodes.add(RoadNode.class);
		links.add(CrossLink.class);
		links.add(RoadLink.class);
		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
		
		List<RoutingGraph> rgs = new ArrayList<RoutingGraph>();
		rgs.add(rg);
		
		Dataset dsi = (Dataset) controller.getInjector().getInstance(DatasetI.class);
		RoutesMapCTAP rm = (RoutesMapCTAP) dsi.getMap(RoutesMap.ROUTES_MAP_KEY);
		rm.addProjections(rgs);
	
		List<SourceRoutesRequest> res = projects.CTAP.geolocClusters.Utils.getSRR_cluster1(rm, "train-intersections-graph-2", 75000);
		
		res = res.stream().limit(10).collect(Collectors.toList());
		
		rm.addSourceRoutesFromNeo4j(res);
		System.out.println();
		rm.close();
	}
	
	@Test
	void agentsMapTest() throws Exception {
		
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		AgentsMap am = controller.getInjector().getInstance(AgentsMap.class);
		am.getAgentsFromNeo4J(StdAgentNodeImpl.class);
		System.out.println();
		
	}
	
	@Test
	void locationsMapTest() throws Exception {
		
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		LocationsMap am = controller.getInjector().getInstance(LocationsMap.class);
		//am.getLocationsFromNeo4J();
		System.out.println();
		
	}
	
	@Test
	void datasetControlerTest() throws Exception {
		
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		DatasetI am = controller.getInjector().getInstance(DatasetI.class);
		System.out.println();
		
	}
}

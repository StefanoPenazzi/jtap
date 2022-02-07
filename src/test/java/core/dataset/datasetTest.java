package core.dataset;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import config.Config;
import controller.Controller;
import core.dataset.RoutesMap.SourceRoutesRequest;
import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.City;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;
import core.graph.routing.RoutingGraph;

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
		nodes.add(City.class);
		nodes.add(RoadNode.class);
		links.add(CrossLink.class);
		links.add(RoadLink.class);
		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
		
		List<RoutingGraph> rgs = new ArrayList<RoutingGraph>();
		rgs.add(rg);
		
		RoutesMap rm = controller.getInjector().getInstance(RoutesMap.class);
		rm.addProjections(rgs);
		City city = new City();
		List<SourceRoutesRequest> srr = new ArrayList<>();
		//srr.add(rm.new SourceRoutesRequest("train-intersections-graph-2",city,"city","Courcouronnes","city","avg_travel_time"));
		srr.add(rm.new SourceRoutesRequest("train-intersections-graph-2",city,"city","Paris","city","avg_travel_time"));
		//rm.addSourceRoutesFromNeo4j(srr);
		rm.addNewRoutesFromJson();
		//rm.saveJson();
		System.out.println();
		rm.close();
	}
}

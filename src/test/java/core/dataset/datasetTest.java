package core.dataset;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import core.dataset.RoutesMap.SourceRoutesRequest;
import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.City;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;
import core.graph.routing.RoutingGraph;

class datasetTest {

	@Test
	void RoutesTest() throws Exception {
		
		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
		List<Class<? extends LinkI>> links = new ArrayList<>();
		nodes.add(City.class);
		nodes.add(RoadNode.class);
		links.add(CrossLink.class);
		links.add(RoadLink.class);
		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
		
		List<RoutingGraph> rgs = new ArrayList<RoutingGraph>();
		rgs.add(rg);
		
		RoutesMap rm = new RoutesMap(rgs);
		City city = new City();
		List<SourceRoutesRequest> srr = new ArrayList<>();
		srr.add(rm.new SourceRoutesRequest("train-intersections-graph-2",city,"city","Courcouronnes","city","avg_travel_time"));
		srr.add(rm.new SourceRoutesRequest("train-intersections-graph-2",city,"city","Paris","city","avg_travel_time"));
		rm.addSourceRoutesFromNeo4j(srr);
		System.out.println();
		rm.close();
		
	}

}

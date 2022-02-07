package core.graph.routing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;

import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.CityNode;
import core.graph.rail.RailLink;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;

class RoutingManagerTest {

	@Test
	void test() throws Exception {
//		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
//		List<Class<? extends LinkI>> links = new ArrayList<>();
//		nodes.add(City.class);
//		nodes.add(RoadNode.class);
//		links.add(CrossLink.class);
//		links.add(RoadLink.class);
//		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
//		RoutingManager rm = new RoutingManager();
//		rm.addNewRoutingGraph(rg);
//		City city = new City();
//		//List<Record> res = rm.getSourceTargetRoute("train-intersections-graph-2",city,city,"city","Paris","city","Pessac","avg_travel_time");
//		long start = System.currentTimeMillis();
//		Map<String,Double> res_1 = rm.getSSSP_AsMap("train-intersections-graph-2",city,"city","Paris","city","avg_travel_time");
//		Map<String,Double> res_2 = rm.getSSSP_AsMap("train-intersections-graph-2",city,"city","Courcouronnes","city","avg_travel_time");
//		long end = System.currentTimeMillis();
//		long elapsedTime = end - start;
//		System.out.println(elapsedTime);
//		rm.close();
		
	}

}

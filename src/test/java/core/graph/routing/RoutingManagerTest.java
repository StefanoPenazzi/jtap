package core.graph.routing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;

import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.City;
import core.graph.rail.RailLink;
import core.graph.rail.gtfs.Stop;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;

class RoutingManagerTest {

	@Test
	void test() throws Exception {
		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
		List<Class<? extends LinkI>> links = new ArrayList<>();
		nodes.add(City.class);
		nodes.add(Stop.class);
		nodes.add(RoadNode.class);
		
		links.add(RailLink.class);
		links.add(CrossLink.class);
		links.add(RoadLink.class);
		
		
		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
		
		RoutingManager rm = new RoutingManager();
		rm.addNewRoutingGraph(rg);
		List<Record> res = rm.getRoute(null, null, null, null, null, null, null);
		System.out.println();
		rm.close();
	}

}

package core.graph.routing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.CityNode;
import core.graph.rail.RailLink;
import core.graph.rail.RailTransferLink;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;

class RoutingGraphTest {

	@Test
	void test() throws Exception {
		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
		List<Class<? extends LinkI>> links = new ArrayList<>();
		nodes.add(CityNode.class);
		nodes.add(RailNode.class);
		nodes.add(RoadNode.class);
		
		links.add(RailLink.class);
		links.add(RailTransferLink.class);
		links.add(CrossLink.class);
		links.add(RoadLink.class);
		
		
		RoutingGraph rg = new RoutingGraph("train-intersections-graph-2",nodes,links,"avg_travel_time");
		rg.graphCaching("france2");
	}

}

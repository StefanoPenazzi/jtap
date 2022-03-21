package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.List;

import config.Config;
import core.dataset.ParameterI;
import core.dataset.ParametersFactoryI;
import core.dataset.RoutesMap;
import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.cross.CrossLink;
import core.graph.geo.CityNode;
import core.graph.rail.RailLink;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;
import core.graph.routing.RoutingGraph;
import core.graph.routing.RoutingManager;

public class Ds2OsParametersFactory extends RoutesMap implements ParametersFactoryI {

	private final Config config;
	private final RoutingManager rm;
	private final String RAIL_ROAD_GRAPH = "rail-road-graph";
	private final String RAIL_GRAPH = "rail-graph";
	private final String ROAD_GRAPH = "road-graph";
	private final List<Long> citiesOs_ids;
	private final List<Long> citiesDs_ids;
	
	
	public Ds2OsParametersFactory(Config config,RoutingManager rm,List<Long> citiesOs_ids,List<Long> citiesDs_ids) {
		super(config, rm);
		this.config = config;
		this.rm = rm;
		this.citiesOs_ids = citiesOs_ids;
		this.citiesDs_ids = citiesDs_ids;
		// TODO Auto-generated constructor stub
	}


	@Override
	public List<ParameterI> run() {
		
		List<ParameterI> res = new ArrayList<>();
		Ds2OsTravelCostParameter ds2osTravelCostParameter = null;
		Ds2OsPathParameter ds2osPathParameter = null;
		/*
		 * projections ---------------------------------------------------------
		 */
		//rail-road-graph
		List<Class<? extends NodeGeoI>> nodesRailRoadGraph = new ArrayList<>();
		List<Class<? extends LinkI>> linksRailRoadGraph = new ArrayList<>();
		nodesRailRoadGraph.add(CityNode.class);
		nodesRailRoadGraph.add(RoadNode.class);
		nodesRailRoadGraph.add(RailNode.class);
		linksRailRoadGraph.add(CrossLink.class);
		linksRailRoadGraph.add(RoadLink.class);
		linksRailRoadGraph.add(RailLink.class);
		try {
			this.addProjection(new RoutingGraph(RAIL_ROAD_GRAPH,nodesRailRoadGraph,linksRailRoadGraph,"avg_travel_time"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//rail-graph
		List<Class<? extends NodeGeoI>> nodesRail = new ArrayList<>();
		List<Class<? extends LinkI>> linksRail = new ArrayList<>();
		nodesRail.add(CityNode.class);
		nodesRail.add(RailNode.class);
		linksRail.add(CrossLink.class);
		linksRail.add(RailLink.class);
		try {
			this.addProjection(new RoutingGraph(RAIL_GRAPH,nodesRail,linksRail,"avg_travel_time"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		//road-graph
		List<Class<? extends NodeGeoI>> nodesRoadGraph = new ArrayList<>();
		List<Class<? extends LinkI>> linksRoadGraph = new ArrayList<>();
		nodesRoadGraph.add(CityNode.class);
		nodesRoadGraph.add(RoadNode.class);
		linksRoadGraph.add(CrossLink.class);
		linksRoadGraph.add(RoadLink.class);
		try {
			this.addProjection(new RoutingGraph(ROAD_GRAPH,nodesRoadGraph,linksRoadGraph,"avg_travel_time"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * SourceRoutesRequest -------------------------------------------------
		 */
		List<SourceRoutesRequest> os2dsRailRoad = new ArrayList<>();
		List<SourceRoutesRequest> os2dsRail = new ArrayList<>();
		List<SourceRoutesRequest> os2dsRoad = new ArrayList<>();
		
		CityNode cityNode = new CityNode();
		citiesDs_ids.forEach(city ->{
			os2dsRailRoad.add(this.new SourceRoutesRequest(RAIL_ROAD_GRAPH,cityNode,city,"avg_travel_time",citiesOs_ids));
			os2dsRail.add(this.new SourceRoutesRequest(RAIL_GRAPH,cityNode,city,"avg_travel_time",citiesOs_ids));
			os2dsRoad.add(this.new SourceRoutesRequest(ROAD_GRAPH,cityNode,city,"avg_travel_time",citiesOs_ids));
		});
		
		/*
		 * Collecting routes ---------------------------------------------------
		 */
		try {
			this.addSourceRoutesWithPathsFromNeo4j(os2dsRailRoad);
			this.addSourceRoutesWithPathsFromNeo4j(os2dsRail);
			this.addSourceRoutesWithPathsFromNeo4j(os2dsRoad);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*
		 * Parameter array -----------------------------------------------------
		 */
		List<List<Long>> parameterDescription = new ArrayList<>();
		List<Long> projections = new ArrayList<>();
		projections.add(0L);
		projections.add(1L);
		projections.add(2L);
		parameterDescription.add(projections);
		parameterDescription.add(citiesDs_ids);
		parameterDescription.add(citiesOs_ids);
		double[][][] parameter = this.toArrayCost(parameterDescription);
		List<Long>[][][] pathParameter = this.toArrayPath(parameterDescription);
		ds2osTravelCostParameter = new Ds2OsTravelCostParameter(parameter,parameterDescription);
		ds2osPathParameter = new Ds2OsPathParameter(pathParameter,parameterDescription);
		res.add(ds2osTravelCostParameter);
		res.add(ds2osPathParameter);
		
		try {
			this.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return res;
	}

}

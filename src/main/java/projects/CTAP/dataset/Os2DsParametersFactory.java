package projects.CTAP.dataset;

import java.util.ArrayList;
import java.util.List;

import config.Config;
import core.dataset.ParameterI;
import core.dataset.ParametersFactoryI;
import core.dataset.RoutesMap;
import core.dataset.RoutesMap.SourceRoutesRequest;
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
import projects.CTAP.graphElements.CTAPTransportLink;

public class Os2DsParametersFactory extends RoutesMap implements ParametersFactoryI {
	
	private final Config config;
	private final RoutingManager rm;
	private final String RAIL_ROAD_GRAPH = "rail-road-graph";
	private final String RAIL_GRAPH = "rail-graph";
	private final String ROAD_GRAPH = "road-graph";
	//TODO cvl question: why no air-road or air-rail or air graphs?!
	private final List<Long> citiesOs_ids;
	private final List<Long> citiesDs_ids;

	public Os2DsParametersFactory(Config config,RoutingManager rm,List<Long> citiesOs_ids,List<Long> citiesDs_ids) {
		super(config, rm);
		this.config = config;
		this.rm = rm;
		this.citiesOs_ids = citiesOs_ids;
		this.citiesDs_ids = citiesDs_ids;
	}

	@Override
	public List<ParameterI> run() {
		List<ParameterI> res = new ArrayList<>();
		Os2DsTravelCostParameter os2dsTravelCostParameter = null;
		Os2DsPathParameter os2dsPathParameter = null;
		/*
		 * projections ---------------------------------------------------------
		 */
		//rail-road-graph
		List<Class<? extends NodeGeoI>> nodesRailRoadGraph = new ArrayList<>();
		List<Class<? extends LinkI>> linksRailRoadGraph = new ArrayList<>();
		nodesRailRoadGraph.add(CityNode.class);//TODO cvl question: this again seems to be making an empty list, but how can that make sense? 
		nodesRailRoadGraph.add(RoadNode.class);
		nodesRailRoadGraph.add(RailNode.class);
		linksRailRoadGraph.add(CTAPTransportLink.class);
		
		try {
			this.addProjection(new RoutingGraph(RAIL_ROAD_GRAPH,nodesRailRoadGraph,linksRailRoadGraph,"weight")); // TODO  cvl question: is this an empty projection?!
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//rail-graph
		List<Class<? extends NodeGeoI>> nodesRail = new ArrayList<>();
		List<Class<? extends LinkI>> linksRail = new ArrayList<>();
		nodesRail.add(CityNode.class);
		nodesRail.add(RailNode.class);
		linksRail.add(CTAPTransportLink.class);
		
		try {
			this.addProjection(new RoutingGraph(RAIL_GRAPH,nodesRail,linksRail,"weight"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		//road-graph
		List<Class<? extends NodeGeoI>> nodesRoadGraph = new ArrayList<>();
		List<Class<? extends LinkI>> linksRoadGraph = new ArrayList<>();
		nodesRoadGraph.add(CityNode.class);
		nodesRoadGraph.add(RoadNode.class);
		linksRoadGraph.add(CTAPTransportLink.class);
	
		try {
			this.addProjection(new RoutingGraph(ROAD_GRAPH,nodesRoadGraph,linksRoadGraph,"weight"));
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
		citiesOs_ids.forEach(city ->{
			os2dsRailRoad.add(this.new SourceRoutesRequest(RAIL_ROAD_GRAPH,cityNode,city,"weight",citiesDs_ids)); //TODO cvl question: why use the string "weight" as the weight property? What does that make the Neo4j router do?!
			os2dsRail.add(this.new SourceRoutesRequest(RAIL_GRAPH,cityNode,city,"weight",citiesDs_ids));
			os2dsRoad.add(this.new SourceRoutesRequest(ROAD_GRAPH,cityNode,city,"weight",citiesDs_ids));
		});
		
		
		/*
		 * Collecting routes ---------------------------------------------------
		 */
		try {
			this.addSourceRoutesWithPathsFromNeo4j(os2dsRailRoad); // cvl comment: these lines eventually call methods that route the path between each OD pair on the specificied network projection
			this.addSourceRoutesWithPathsFromNeo4j(os2dsRail); // TODO cvl question: I am not sure which container these route (?) objects are being added to...
			this.addSourceRoutesWithPathsFromNeo4j(os2dsRoad);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * Parameter array -----------------------------------------------------
		 */
		List<List<Long>> parameterDescription = new ArrayList<>();
		List<Long> projections = new ArrayList<>();
		projections.add(0L); //cvl comment: adding number 0 to index 0, as Long
		projections.add(1L);
		projections.add(2L);
		parameterDescription.add(projections);
		parameterDescription.add(citiesOs_ids);
		parameterDescription.add(citiesDs_ids);
		double[][][] travelCostParameter = this.toArrayCost(parameterDescription);
		List<Long>[][][] pathParameter = this.toArrayPath(parameterDescription);
		os2dsTravelCostParameter = new Os2DsTravelCostParameter(travelCostParameter,parameterDescription);
		os2dsPathParameter = new Os2DsPathParameter(pathParameter,parameterDescription);
		res.add(os2dsTravelCostParameter);
		res.add(os2dsPathParameter);
		//TODO cvl question: Jeronimo is concerned that the solver does not recieve the routes from all the network projections - only the least cost one.
		// this would imply that mode choice is rather limited....all agents would choose the same route. So far, I do not see this happening. 
		// it looks like all the projections (in this class, road, rail, and rail-road) are being passed to the objected being returned by this method, namely "res". 
		// Is there something I missed in the other classes and methods being called? 
		
		try {
			this.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return res;
	}

}

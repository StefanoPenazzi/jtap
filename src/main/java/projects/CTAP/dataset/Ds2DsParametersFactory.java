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
import projects.CTAP.graphElements.CTAPTransportLink;

public class Ds2DsParametersFactory extends RoutesMap implements ParametersFactoryI{
	
	private final Config config;
	private final RoutingManager rm;
	private final String RAIL_ROAD_GRAPH = "rail-road-graph";
	private final String RAIL_GRAPH = "rail-graph";
	private final String ROAD_GRAPH = "road-graph";
	private final List<Long> citiesDs_ids;

	public Ds2DsParametersFactory(Config config,RoutingManager rm, List<Long> citiesDs_ids) {
		super(config, rm);
		this.config = config;
		this.rm = rm;
		this.citiesDs_ids = citiesDs_ids; 
	}
	
	public List<ParameterI> run(){
		    List<ParameterI> res = new ArrayList<>();
		    Ds2DsTravelCostParameter ds2dsTravelCostParameter = null;
		    Ds2DsPathParameter ds2dsPathParameter = null;
			/*
			 * projections ---------------------------------------------------------
			 */
			//rail-road-graph
			List<Class<? extends NodeGeoI>> nodesRailRoadGraph = new ArrayList<>();
			List<Class<? extends LinkI>> linksRailRoadGraph = new ArrayList<>();
			nodesRailRoadGraph.add(CityNode.class);
			nodesRailRoadGraph.add(RoadNode.class);
			nodesRailRoadGraph.add(RailNode.class);
			linksRailRoadGraph.add(CTAPTransportLink.class);
			
			try {
				this.addProjection(new RoutingGraph(RAIL_ROAD_GRAPH,nodesRailRoadGraph,linksRailRoadGraph,"weight"));
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
			citiesDs_ids.forEach(city ->{
				os2dsRailRoad.add(this.new SourceRoutesRequest(RAIL_ROAD_GRAPH,cityNode,city,"weight",citiesDs_ids));
				os2dsRail.add(this.new SourceRoutesRequest(RAIL_GRAPH,cityNode,city,"weight",citiesDs_ids));
				os2dsRoad.add(this.new SourceRoutesRequest(ROAD_GRAPH,cityNode,city,"weight",citiesDs_ids));
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
			parameterDescription.add(citiesDs_ids);
			double[][][] travelCostParameter = this.toArrayCost(parameterDescription);
			List<Long>[][][] pathParameter = this.toArrayPath(parameterDescription);
			ds2dsTravelCostParameter = new Ds2DsTravelCostParameter(travelCostParameter,parameterDescription);
			ds2dsPathParameter = new Ds2DsPathParameter(pathParameter,parameterDescription);
			res.add(ds2dsTravelCostParameter);
			res.add(ds2dsPathParameter);
			
			try {
				this.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			return res;
	}

}

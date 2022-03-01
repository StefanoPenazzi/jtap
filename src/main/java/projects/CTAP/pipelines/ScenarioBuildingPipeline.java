package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.Config;
import controller.Controller;
import core.dataset.Dataset;
import core.dataset.DatasetI;
import core.dataset.RoutesMap;
import core.dataset.RoutesMap.SourceRoutesRequest;
import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.Activity.ActivityNode;
import core.graph.cross.CrossLink;
import core.graph.facility.osm.FacilityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import core.graph.rail.RailLink;
import core.graph.rail.gtfs.GTFS;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadLink;
import core.graph.road.osm.RoadNode;
import core.graph.routing.RoutingGraph;
import picocli.CommandLine;

public class ScenarioBuildingPipeline implements Callable<Integer> {
	
	@CommandLine.Command(
			name = "JTAP",
			description = "",
			showDefaultValues = true,
			mixinStandardHelpOptions = true
	)
	
	@CommandLine.Option(names = {"--configFile","-cf"}, description = "The .xml file containing the configurations")
	private Path configFile;
	
	@CommandLine.Option(names = "--threads", defaultValue = "4", description = "Number of threads to use concurrently")
	private int threads;
	
	private static final Logger log = LogManager.getLogger(ScenarioBuildingPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new ScenarioBuildingPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		//Road------------------------------------------------------------------
		core.graph.road.osm.Utils.setOSMRoadNetworkIntoNeo4j();
		
		//insert GTFS-----------------------------------------------------------
		GTFS gtfs = controller.getInjector().getInstance(GTFS.class);
		core.graph.rail.Utils.deleteRailGTFS();
		core.graph.rail.Utils.insertRailGTFSintoNeo4J(gtfs,"2021-07-18");
		
		
		//insert cities---------------------------------------------------------
		core.graph.geo.Utils.insertCitiesIntoNeo4JFromCsv(CityNode.class);
		
		//create FacilityNodes from osm-----------------------------------------
		core.graph.facility.osm.Utils.facilitiesIntoNeo4j(config);
		
		//connect FacilityNodes with Cities-------------------------------------
		Map<Class<? extends NodeGeoI>,String> facilityConnMap = new HashMap<>();
		facilityConnMap.put(CityNode.class,"city");
		core.graph.Utils.setShortestDistCrossLink(FacilityNode.class,"node_osm_id",facilityConnMap,3);
		
		//create the CityFacStatNodes-------------------------------------------
		core.graph.geo.Utils.addCityFacStatNode();
		
		//Connections between RoadNetwork and RailNetwork-----------------------
		Map<Class<? extends NodeGeoI>,String> railConnMap = new HashMap<>();
		railConnMap.put(RoadNode.class,"node_osm_id");
		core.graph.Utils.setShortestDistCrossLink(RailNode.class,"id",railConnMap,2);
		
		//Connections between Cities and RoadNetwork/RailNetwork----------------
		Map<Class<? extends NodeGeoI>,String> cityConnMap = new HashMap<>();
		cityConnMap.put(RoadNode.class,"node_osm_id");
		cityConnMap.put(RailNode.class, "id");
		core.graph.Utils.setShortestDistCrossLink(CityNode.class,"city",cityConnMap,3);
		
		//insert activities-----------------------------------------------------
		core.graph.Activity.Utils.insertActivitiesFromCsv(ActivityNode.class);
		
		//insert population-----------------------------------------------------
		core.graph.population.Utils.insertStdPopulationFromCsv(StdAgentNodeImpl.class);
		
		//insert attractiveness-------------------------------------------------
		projects.CTAP.attractiveness.normalized.Utils.insertAttractivenessNormalizedIntoNeo4j();
		
		//OD MATRIX-------------------------------------------------------------
		saveODMatrix();
		
		
		return 1;
	}
	
	public static void saveODMatrix() throws Exception {
//		List<Class<? extends NodeGeoI>> nodes = new ArrayList<>();
//		List<Class<? extends LinkI>> links = new ArrayList<>();
//		nodes.add(CityNode.class);
//		nodes.add(RoadNode.class);
//		nodes.add(RailNode.class);
//		links.add(CrossLink.class);
//		links.add(RoadLink.class);
//		links.add(RailLink.class);
//		RoutingGraph rg = new RoutingGraph("rail-road-graph",nodes,links,"avg_travel_time");
//		List<RoutingGraph> rgs = new ArrayList<RoutingGraph>();
//		rgs.add(rg);
//		Dataset dsi = (Dataset) Controller.getInjector().getInstance(DatasetI.class);
//		RoutesMapCTAP rm = (RoutesMapCTAP) dsi.getMap(RoutesMap.ROUTES_MAP_KEY);
//		rm.addProjections(rgs);
//		List<SourceRoutesRequest> res = projects.CTAP.geolocClusters.Utils.getSRR_cluster1(rm,
//				"rail-road-graph",
//				Controller.getConfig().getCtapModelConfig().getPopulationThreshold());
//		res = res.stream().skip(60).limit(3).collect(Collectors.toList());
//		rm.addSourceRoutesFromNeo4j(res);
//		rm.saveJson();
//		rm.close();
	}
	
}

package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.Config;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.Activity.ActivityNode;
import core.graph.facility.osm.FacilityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import core.graph.rail.gtfs.GTFS;
import core.graph.rail.gtfs.RailNode;
import core.graph.road.osm.RoadNode;
import picocli.CommandLine;
import projects.CTAP.attractiveness.normalized.DefaultAttractivenessModelImpl;
import projects.CTAP.attractiveness.normalized.DefaultAttractivenessModelVarImpl;
import projects.CTAP.graphElements.ActivityCityLink;
import projects.CTAP.transport.DefaultCTAPTransportLinkFactory;

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
		facilityConnMap.put(CityNode.class,"city_id");
		core.graph.Utils.setShortestDistCrossLink(FacilityNode.class,"node_osm_id",facilityConnMap,3);
		
		//create the CityFacStatNodes-------------------------------------------
		core.graph.geo.Utils.addCityFacStatNodeWithHistorical();
		
		//Connections between RoadNetwork and RailNetwork-----------------------
		Map<Class<? extends NodeGeoI>,String> railConnMap = new HashMap<>();
		railConnMap.put(RoadNode.class,"node_osm_id");
		core.graph.Utils.setShortestDistCrossLink(RailNode.class,"stop_id",railConnMap,2);
		
		//Connections between Cities and RoadNetwork/RailNetwork----------------
		Map<Class<? extends NodeGeoI>,String> cityConnMap = new HashMap<>();
		cityConnMap.put(RoadNode.class,"node_osm_id");
		cityConnMap.put(RailNode.class, "stop_id");
		core.graph.Utils.setShortestDistCrossLink(CityNode.class,"city_id",cityConnMap,3);
		
		//insert activities-----------------------------------------------------
		core.graph.Activity.Utils.insertActivitiesFromCsv(ActivityNode.class);
		core.graph.Activity.Utils.insertActivitiesLocFromCsv(ActivityCityLink.class);
		
		//insert population-----------------------------------------------------
		core.graph.population.Utils.insertStdPopulationFromCsv(StdAgentNodeImpl.class);
		
		//insert attractiveness-------------------------------------------------
		projects.CTAP.attractiveness.normalized.Utils.insertAttractivenessNormalizedIntoNeo4j(
				(DefaultAttractivenessModelImpl)Controller.getInjector().getInstance(DefaultAttractivenessModelImpl.class),
				new DefaultAttractivenessModelVarImpl());
		
		//insert transport links------------------------------------------------
		DefaultCTAPTransportLinkFactory ctapTranspFactory = new DefaultCTAPTransportLinkFactory();
		ctapTranspFactory.insertCTAPTransportLinkFactory(config.getCtapModelConfig()
				.getTransportConfig().getCtapTransportLinkConfig());
		
		return 1;
	}
}

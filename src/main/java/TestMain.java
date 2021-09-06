import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.Config;
import config.GTFSConfig;
import controller.Controller;
import core.graph.NodeGeoI;
import core.graph.geo.City;
import core.graph.rail.Utils;
import core.graph.rail.gtfs.GTFS;
import core.graph.rail.gtfs.Stop;
import core.graph.road.osm.Intersection;
import core.graph.road.osm.RoadNode;
import picocli.CommandLine;

public class TestMain implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(TestMain.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new TestMain()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		String db = "france2";
		
		//Road
		core.graph.road.osm.Utils.setOSMRoadNetworkIntoNeo4j(db);
		
		//insert GTFS
		GTFS gtfs = controller.getInjector().getInstance(GTFS.class);
		core.graph.rail.Utils.deleteRailGTFS(db);
		core.graph.rail.Utils.insertRailGTFSintoNeo4J(gtfs,db,controller.getInjector().getInstance(Config.class));
		
		//insert cities
		core.graph.geo.Utils.insertCitiesIntoNeo4JFromCsv(db,controller.getInjector().getInstance(Config.class),City.class);
		
		//connections between subgraphs
		Map<Class<? extends NodeGeoI>,String> railConnMap = new HashMap<>();
		railConnMap.put(RoadNode.class,"node_osm_id");
		//railConnMap.put(City.class, "city");
		core.graph.Utils.setShortestDistCrossLink(db, config.getGeneralConfig().getTempDirectory(),Stop.class,"id",railConnMap,true);
		
		Map<Class<? extends NodeGeoI>,String> cityConnMap = new HashMap<>();
		cityConnMap.put(RoadNode.class,"node_osm_id");
		cityConnMap.put(Stop.class, "id");
		core.graph.Utils.setShortestDistCrossLink(db, config.getGeneralConfig().getTempDirectory(),City.class,"city",cityConnMap,true);
		
//		Map<Class<? extends NodeGeoI>,String> intersectionConnMap = new HashMap<>();
//		intersectionConnMap.put(City.class,"node_osm_id");
//		intersectionConnMap.put(Stop.class, "id");
//		core.graph.Utils.setShortestDistCrossLink("france2", config.getGeneralConfig().getTempDirectory(),Intersection.class,"node_osm_id",intersectionConnMap);
		
		return 1;
	}
	
}

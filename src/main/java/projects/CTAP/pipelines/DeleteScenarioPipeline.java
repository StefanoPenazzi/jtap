package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.Config;
import controller.Controller;
import picocli.CommandLine;
import projects.CTAP.graphElements.AttractivenessNormalizedLink;
import projects.CTAP.graphElements.CTAPTransportLink;


public class DeleteScenarioPipeline implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(DeleteScenarioPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new DeleteScenarioPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		//delete transport links------------------------------------------------
		data.external.neo4j.Utils.deleteLinks(CTAPTransportLink.class);
		
		//delete attractiveness-------------------------------------------------
		//data.external.neo4j.Utils.deleteLinks(AttractivenessNormalizedLink.class);
		
		//delete population-----------------------------------------------------
		//core.graph.population.Utils.deletePopulation();
		
		//delete activities-----------------------------------------------------
		//core.graph.Activity.Utils.deleteActivities();
		
		//delete CityFacStatNodes-----------------------------------------------
		core.graph.geo.Utils.deleteCityFacStatNode();
		
		//delete FacilityNodes from osm-----------------------------------------
		//core.graph.facility.osm.Utils.deleteFacilities();
		
		//delete GTFS-----------------------------------------------------------
		//core.graph.rail.Utils.deleteRailGTFS();
		
		//delete AIR NETWORK-----------------------------------------------------------
		//core.graph.air.Utils.deleteAirNetwork();
		
		//delete cities---------------------------------------------------------
		//core.graph.geo.Utils.deleteCities();
		
		//road network can not be deleted. In case a new road network needs to be updated a new database must be used  
		
		return 1;
	}
}

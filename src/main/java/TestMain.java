import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.Config;
import config.GTFSConfig;
import controller.Controller;
import data.external.rail.GTFS;
import data.external.rail.Utils;
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
		GTFS gtfs = controller.getInjector().getInstance(GTFS.class);
		Utils.deleteGTFS("osm");
		Utils.insertGTFSintoNEO4J(gtfs,"osm",controller.getInjector().getInstance(Config.class),"Intersection");
		return 1;
	}
	
}

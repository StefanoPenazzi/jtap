package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.Config;
import controller.Controller;
import picocli.CommandLine;
import projects.CTAP.dataset.Dataset;
import projects.CTAP.dataset.DatasetJsonFactory;


public class DatasetImportPipeline implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(DatasetImportPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new DatasetImportPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		DatasetJsonFactory dsf = new DatasetJsonFactory();
		Dataset ds = dsf.run();
		System.out.print(false);
        
		return 1;
	}

}

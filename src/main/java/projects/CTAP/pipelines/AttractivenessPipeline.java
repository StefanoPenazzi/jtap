package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.Config;
import controller.Controller;
import picocli.CommandLine;
import projects.CTAP.attractiveness.normalized.DefaultAttractivenessModelImpl;
import projects.CTAP.attractiveness.normalized.DefaultAttractivenessModelVarImpl;


public class AttractivenessPipeline implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(AttractivenessPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new AttractivenessPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		//insert attractiveness-------------------------------------------------
		projects.CTAP.attractiveness.normalized.Utils.insertAttractivenessNormalizedIntoNeo4j(
				(DefaultAttractivenessModelImpl)Controller.getInjector().getInstance(DefaultAttractivenessModelImpl.class),
				new DefaultAttractivenessModelVarImpl());
		
		return 1;
	}
}

package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.Config;
import controller.Controller;
import picocli.CommandLine;
import projects.CTAP.transport.DefaultCTAPTransportLinkFactory;

public class TransportLinkPipeline implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(TransportLinkPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new TransportLinkPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		//insert transport links------------------------------------------------
		DefaultCTAPTransportLinkFactory ctapTranspFactory = new DefaultCTAPTransportLinkFactory();
		ctapTranspFactory.insertCTAPTransportLinkFactory(config.getCtapModelConfig()
				.getTransportConfig().getCtapTransportLinkConfig());
		
		return 1;
	}
}

package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import config.Config;
import controller.Controller;
import core.dataset.DatasetFactoryI;
import core.dataset.DatasetI;
import core.population.PopulationFactoryI;
import core.solver.SolverImpl;
import picocli.CommandLine;
import projects.CTAP.population.Agent;
import projects.CTAP.population.Population;
import projects.CTAP.population.PopulationFactory;

public class SingleAgentOptPlanPipeline  implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(SingleAgentOptPlanPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new SingleAgentOptPlanPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of (configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		PopulationFactoryI populationFactory = controller.getInjector().getInstance(PopulationFactoryI.class);
		DatasetFactoryI datasetFactory = controller.getInjector().getInstance(DatasetFactoryI.class);
		DatasetI dataset = datasetFactory.run();
		Population population = (Population) populationFactory.run(dataset);
		Agent ag = (Agent) population.getAgentsIterator().next();
		
		double[] initialGuess = new double[] {0,2000,4000,5000,6000,7000,10000,15000,20000,22000,1999,3999,4999,5999,6999,7999,14999,19999,21999,22999};
		SolverImpl si = new SolverImpl.Builder((ag.getAgentModels().get(0)))
				.initialGuess(initialGuess)
				.build();
		si.run();
		
		return 1;
		
	}

}

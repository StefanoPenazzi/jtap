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
import picocli.CommandLine;
import projects.CTAP.outputAnalysis.LinkTimeFlow;
import projects.CTAP.outputAnalysis.LinkTimeFlowDatasetJsonFactory;
import projects.CTAP.population.Population;
import projects.CTAP.population.PopulationSingleAgentFactory;
import projects.CTAP.solver.Solver;

public class ParallelAgentOptPlanPipeline implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(ParallelAgentOptPlanPipeline.class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new ParallelAgentOptPlanPipeline()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of (configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		//PopulationFactoryI populationFactory = controller.getInjector().getInstance(PopulationFactoryI.class);
		PopulationSingleAgentFactory populationFactory = controller.getInjector().getInstance(PopulationSingleAgentFactory .class);
		DatasetFactoryI datasetFactory = controller.getInjector().getInstance(DatasetFactoryI.class);
		DatasetI dataset = datasetFactory.run();
		Population population = (Population) populationFactory.run(dataset);
		
		Solver ctapSolver = controller.getInjector().getInstance(Solver.class);
		ctapSolver.run(population,dataset);
		population.save();
		
		LinkTimeFlowDatasetJsonFactory lfd = controller.getInjector().getInstance(LinkTimeFlowDatasetJsonFactory.class);
		LinkTimeFlow ltf = new LinkTimeFlow(population,336d,lfd.run(),config);
		ltf.run();
		ltf.saveDb();
		
		return 1;
		
	}

}

package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.Config;
import controller.Controller;
import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import core.graph.routing.RoutingManager;
import picocli.CommandLine;
import projects.CTAP.dataset.AgentActivityParameterFactory;
import projects.CTAP.dataset.AgentParametersFactory;
import projects.CTAP.dataset.Os2DsTravelCostDbParameterFactory;

public class DatasetBuildingPipeline implements Callable<Integer> {
	
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
	
	private static final Logger log = LogManager.getLogger(DatasetBuildingPipeline .class);

	public static void main(String[] args) {
		System.exit(new CommandLine(new DatasetBuildingPipeline ()).execute(args));
	}

	@Override
	public Integer call() throws Exception {
		
		Config config = Config.of(configFile.toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
        RoutingManager rm = controller.getInjector().getInstance(RoutingManager.class);
		
		//indexes---------------------------------------------------------------
		List<Long> agents_ids = data.external.neo4j.Utils.importNodes(StdAgentNodeImpl.class).stream().map(x -> x.getId()).collect(Collectors.toList());
		List<Long> activities_ids = data.external.neo4j.Utils.importNodes(ActivityNode.class).stream().map(x -> x.getActivityId()).collect(Collectors.toList());
		List<CityNode> cities = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),CityNode.class);
		Integer popThreshold = Controller.getConfig().getCtapModelConfig().getPopulationThreshold();
		List<Long> citiesDs_ids = cities.stream().filter(e -> e.getPopulation() >= popThreshold).limit(2).map(CityNode::getId).collect(Collectors.toList());
		List<Long> citiesOs_ids = cities.stream().filter(e -> e.getPopulation() < popThreshold).limit(2).map(CityNode::getId).collect(Collectors.toList());
		List<Long> time = new ArrayList<>() {{add(0L); add(1L);}};
		
		//factories-------------------------------------------------------------
		List<? extends ParameterFactoryI> agentActivtyParams = AgentActivityParameterFactory.getAgentActivityParameterFactories(agents_ids,activities_ids);
		List<? extends ParameterFactoryI> agentParams = AgentParametersFactory.getAgentParameterFactories(agents_ids);
		Os2DsTravelCostDbParameterFactory os = new Os2DsTravelCostDbParameterFactory(config,rm,citiesOs_ids,citiesDs_ids);
		List<ParameterFactoryI> prsFactories = Stream.of(agentActivtyParams, agentParams)
                 .flatMap(x -> x.stream())
                 .collect(Collectors.toList());
		prsFactories.add(os);
		
		//parameters------------------------------------------------------------
		List<ParameterI> prs = new ArrayList<>();
		for(ParameterFactoryI pi:prsFactories) {
			prs.add(pi.run());
		}
		for(ParameterI pr:prs) {
			pr.save();
		}
		
		return 1;
	}

}

package projects.CTAP.pipelines;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.Config;
import controller.Controller;
import core.dataset.ModelElementI;
import core.dataset.ParameterFactoryI;
import core.dataset.ParameterI;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import core.graph.routing.RoutingManager;
import picocli.CommandLine;
import projects.CTAP.dataset.ActivitiesIndex;
import projects.CTAP.dataset.AgentActivityParameterFactory;
import projects.CTAP.dataset.AgentParametersFactory;
import projects.CTAP.dataset.AgentsIndex;
import projects.CTAP.dataset.AttractivenessParameterFactory;
import projects.CTAP.dataset.CitiesDsIndex;
import projects.CTAP.dataset.CitiesOsIndex;
import projects.CTAP.dataset.Ds2DsTravelCostDbParameterFactory;
import projects.CTAP.dataset.Ds2OsTravelCostDbParameterFactory;
import projects.CTAP.dataset.Os2DsTravelCostDbParameterFactory;
import projects.CTAP.dataset.TimeIndex;

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
		
		Config config = Config.of (Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		
		RoutingManager rm = controller.getInjector().getInstance(RoutingManager.class);
		
		//indexes
		List<Long> agents_ids = data.external.neo4j.Utils.importNodes(StdAgentNodeImpl.class).stream().map(x -> x.getId()).collect(Collectors.toList());
		List<Long> activities_ids = data.external.neo4j.Utils.importNodes(ActivityNode.class).stream().map(x -> x.getActivityId()).collect(Collectors.toList());
		List<CityNode> cities = data.external.neo4j.Utils.importNodes(config.getNeo4JConfig().getDatabase(),CityNode.class);
		Integer popThreshold = Controller.getConfig().getCtapModelConfig().getPopulationThreshold();
		List<Long> citiesDs_ids = cities.stream().filter(e -> e.getPopulation() >= popThreshold).limit(2).map(CityNode::getId).collect(Collectors.toList());
		List<Long> citiesOs_ids = cities.stream().filter(e -> e.getPopulation() < popThreshold).limit(2).map(CityNode::getId).collect(Collectors.toList());
		Integer initialTime = config.getCtapModelConfig().getAttractivenessModelConfig().getAttractivenessNormalizedConfig().getInitialTime();
		Integer finalTime = config.getCtapModelConfig().getAttractivenessModelConfig().getAttractivenessNormalizedConfig().getFinalTime();
		Integer intervalTime = config.getCtapModelConfig().getAttractivenessModelConfig().getAttractivenessNormalizedConfig().getIntervalTime();
		List<Long> time = LongStream.iterate(initialTime,i->i+intervalTime).limit(Math.round(finalTime/intervalTime)).boxed().collect(Collectors.toList());
		
		//factories
		//indexes
		AgentsIndex agentIndex = new AgentsIndex(agents_ids);
		ActivitiesIndex activitiesIndex = new ActivitiesIndex(activities_ids);
		CitiesDsIndex citiesDsIndex = new CitiesDsIndex(citiesDs_ids);
		CitiesOsIndex citiesOsIndex = new CitiesOsIndex(citiesOs_ids);
		TimeIndex timeIndex = new TimeIndex(time);
		//params
		List<? extends ParameterFactoryI> agentActivtyParams = AgentActivityParameterFactory.getAgentActivityParameterFactories(agents_ids,activities_ids);
		List<? extends ParameterFactoryI> agentParams = AgentParametersFactory.getAgentParameterFactories(agents_ids);
		AttractivenessParameterFactory attractivenessParams = new AttractivenessParameterFactory(agents_ids,activities_ids,citiesDs_ids,time);
		Os2DsTravelCostDbParameterFactory osds = new Os2DsTravelCostDbParameterFactory(config,rm,citiesOs_ids,citiesDs_ids);
		Ds2OsTravelCostDbParameterFactory dsos = new Ds2OsTravelCostDbParameterFactory(config,rm,citiesOs_ids,citiesDs_ids);
		Ds2DsTravelCostDbParameterFactory dsds = new Ds2DsTravelCostDbParameterFactory(config,rm,citiesDs_ids);
		
		List<ParameterFactoryI> res = Stream.of(agentActivtyParams, agentParams)
                 .flatMap(x -> x.stream())
                 .collect(Collectors.toList());
		
		res.add(attractivenessParams);
		res.add(osds);
		res.add(dsos);
		res.add(dsds);
		
		//parameters
		List<ModelElementI> prs = new ArrayList<>();
		
		for(ParameterFactoryI pi:res) {
			prs.add(pi.run());
		}
		prs.add(agentIndex);
		prs.add(activitiesIndex);
		prs.add(citiesDsIndex);
		prs.add(citiesOsIndex);
		prs.add(timeIndex);
		
		for(ModelElementI pr:prs) {
			pr.save();
		}
		
		rm.close();
		
		return 1;
	}

}

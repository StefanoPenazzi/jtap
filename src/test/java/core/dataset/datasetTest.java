package core.dataset;

import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;
import core.graph.Activity.ActivityNode;
import core.graph.geo.CityNode;
import core.graph.population.StdAgentNodeImpl;
import core.graph.routing.RoutingManager;
import projects.CTAP.dataset.AgentActivityParameterFactory;
import projects.CTAP.dataset.AgentParametersFactory;
import projects.CTAP.dataset.AttractivenessParameterFactory;
import projects.CTAP.dataset.Os2DsTravelCostParameter;
import projects.CTAP.dataset.Os2DsTravelCostDbParameterFactory;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class datasetTest {
	
	@Test
	void totTest() throws Exception {
		
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
		List<Long> time = new ArrayList<>() {{add(0L); add(1L);}};
		
		//factories
		List<? extends ParameterFactoryI> agentActivtyParams = AgentActivityParameterFactory.getAgentActivityParameterFactories(agents_ids,activities_ids);
		List<? extends ParameterFactoryI> agentParams = AgentParametersFactory.getAgentParameterFactories(agents_ids);
		//AttractivenessParameterFactory attractivenessParams = new AttractivenessParameterFactory(agents_ids,activities_ids,citiesDs_ids,time);
		Os2DsTravelCostDbParameterFactory os = new Os2DsTravelCostDbParameterFactory(config,rm,citiesOs_ids,citiesDs_ids);
		
		List<ParameterFactoryI> res = Stream.of(agentActivtyParams, agentParams)
                 .flatMap(x -> x.stream())
                 .collect(Collectors.toList());
		
		//res.add(attractivenessParams);
		res.add(os);
		
		//parameters
		List<ParameterI> prs = new ArrayList<>();
		
		for(ParameterFactoryI pi:res) {
			prs.add(pi.run());
		}
		
		for(ParameterI pr:prs) {
			pr.save();
		}
		
		System.out.println();
	}

}

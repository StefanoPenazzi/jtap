package projects.CTAP.model;

import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;
import projects.CTAP.population.Agent;
import projects.CTAP.population.Population;
import projects.CTAP.population.PopulationFactory;

class AgentTest {

	@Test
	void test() {
		Config config = Config.of(Paths.get("/home/stefanopenazzi/projects/jtap/config_.xml").toFile()); 
		Controller controller = new Controller(config);
		controller.run();
		controller.emptyTempDirectory();
		PopulationFactory populationFactory = controller.getInjector().getInstance(PopulationFactory.class);
		Population population = populationFactory.run();
		Agent ag = (Agent) population.getAgentsIterator().next();
		
		double[] ts = new double[] {0,2000,4000,5000,6000,7000,10000,15000,20000,22000};
		double[] te = new double[] {0,3999,4999,5999,6999,7999,14999,19999,21999,22999};
		double res = ((ObjectiveFunctionCTAP)ag.getAgentModels().get(0).getObjectiveFunction()).getValue(ts,te);
		System.out.println();
	    
	}

}

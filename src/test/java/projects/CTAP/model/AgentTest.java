package projects.CTAP.model;

import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import config.Config;
import controller.Controller;
import core.solver.SolverImpl;
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
		
		double[] initialGuess = new double[] {0,2000,4000,5000,6000,7000,10000,15000,20000,22000,1999,3999,4999,5999,6999,7999,14999,19999,21999,22999};
		SolverImpl si = new SolverImpl.Builder((ag.getAgentModels().get(0)))
				.initialGuess(initialGuess)
				.build();
		si.run();
		
		System.out.println();
	    
	}

}

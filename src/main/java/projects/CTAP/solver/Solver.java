package projects.CTAP.solver;

import com.google.inject.Inject;

import config.Config;
import core.population.PopulationI;
import core.solver.SolverImpl;
import projects.CTAP.population.Agent;
import projects.CTAP.population.Population;

public class Solver {
	
	private Config config;
	
	@Inject
	public Solver(Config config) {
		this.config = config;
	}
	
	
	public void run(Population population) {
		
		
		//parallelization
		
		//split the agent list
		
        Agent ag = (Agent) population.getAgentsIterator().next();
		
		double[] initialGuess = new double[] {0,2000,4000,5000,6000,7000,10000,15000,20000,22000,1999,3999,4999,5999,6999,7999,14999,19999,21999,22999};
		SolverImpl si = new SolverImpl.Builder((ag.getAgentModels().get(0)))
				.initialGuess(initialGuess)
				.build();
		si.run();
		
	}

}

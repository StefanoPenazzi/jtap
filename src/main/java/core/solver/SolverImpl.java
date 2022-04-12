package core.solver;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import core.models.ModelI;
import projects.CTAP.model.LowerBoundCTAP;
import projects.CTAP.model.UpperBoundCTAP;

public class SolverImpl implements SolverI {
	
	private MultivariateOptimizer optimizer;
	private ModelI model;
	private double[] initialGuess = null;
	
	public static class Builder {
		
		private ModelI model;
		private double[] initialGuess_ = null;
	
		public Builder(ModelI model) {
			this.model = model;
		}
		
		public Builder initialGuess(double[] initialGuess_) {
			this.initialGuess_ = initialGuess_;
			return this;
		}
		
		public SolverImpl build() {
			SolverImpl solver = new SolverImpl(this.model);
			solver.initialGuess = this.initialGuess_;
			return solver;
		}
	}
	
	public SolverImpl(ModelI model) {
		this.model = model;
		//optimizer = new NelderMeadSimplex(model.getObjectiveFunction().getVariablesLength());
		ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1.e-6, 1.e-14);
        optimizer = new CMAESOptimizer(1000, 0.01, true, 0, 1, new MersenneTwister(), true,
                convergenceChecker);
	}
	
	@Override
	public PointValuePair run() {
		org.apache.commons.math3.optim.PointValuePair pvp = null;
		double[] lower = ((LowerBoundCTAP)this.model.getConstraints().get(0)).getConstraint();
		double[] upper = ((UpperBoundCTAP)this.model.getConstraints().get(1)).getConstraint();
		pvp = optimizer.optimize(
				new ObjectiveFunction(new MultivariateFunctionSolver(this.model)),
				GoalType.MINIMIZE,
				new MaxIter(1000),
				MaxEval.unlimited(),
				new InitialGuess(this.initialGuess),
				new CMAESOptimizer.PopulationSize(3),
				new SimpleBounds(lower, upper),
				new CMAESOptimizer.Sigma(upper)
				); //
		return pvp;
	}
	
	class MultivariateFunctionSolver implements MultivariateFunction{
		private ModelI model;
		
		 MultivariateFunctionSolver(ModelI model){
			 this.model = model;
		 }
		
		@Override
		public double value(double[] point) {
			return this.model.getObjectiveFunction().getValue(point);
		}
	}
}

package core.solver;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;

import core.models.ModelI;
import core.solver.NelderMeadTest.MultivariateFunctionTest1;
import projects.CTAP.model.LowerBoundCTAP;
import projects.CTAP.model.UpperBoundCTAP;

public class SolverImpl implements SolverI {
	
	private NelderMeadSimplex optimizer;
 	private SimpleValueChecker sc = new SimpleValueChecker(-1,-1,100000);
	private SimplexOptimizer so = new SimplexOptimizer(sc);
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
		optimizer = new NelderMeadSimplex(model.getObjectiveFunction().getVariablesLength());
	}
	

	@Override
	public Object run() {
		
		org.apache.commons.math3.optim.PointValuePair pvp = null;
		double[] lower = ((LowerBoundCTAP)this.model.getConstraints().get(0)).getConstraint();
		double[] upper = ((UpperBoundCTAP)this.model.getConstraints().get(1)).getConstraint();
		if(this.initialGuess != null) {
			pvp = so.optimize(optimizer, 
					new ObjectiveFunction(new MultivariateFunctionSolver_1(
							new MultivariateFunctionSolver(this.model),lower,upper)),
					GoalType.MINIMIZE,new MaxIter(1000000),new MaxEval(1000000),
					new InitialGuess(this.initialGuess)); //
		}
		else {
			this.initialGuess = new double[this.model.getObjectiveFunction().getVariablesLength()];
			pvp = so.optimize(optimizer, 
					new ObjectiveFunction(new MultivariateFunctionSolver_1(
							new MultivariateFunctionSolver(this.model),lower,upper)),
					GoalType.MINIMIZE,new MaxIter(1000000),new MaxEval(1000000),
					new InitialGuess(this.initialGuess));
		}
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
	
	class MultivariateFunctionSolver_1 extends MultivariateFunctionMappingAdapter{
		public MultivariateFunctionSolver_1(MultivariateFunction bounded, double[] lower, double[] upper) {
			super(bounded, lower, upper);
		}
	}

}

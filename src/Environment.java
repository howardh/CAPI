
import java.util.HashMap;

/**
 * Only usable for environments with a finite state and action space
 */
public abstract class Environment
{
	double gamma = 0.99; //Used for value computations
	final double theta = 1e-24;	//Max error
	protected HashMap<StateActionPair,Double> actionValue;	//Optimal Q
	protected HashMap<State,Double> stateValue;				//Optimal V
	
	//Optimal value computation functions
	public void computeValues()
	{
		//Must be called in this order. Action values are used to calculate state values.
		this.computeActionValues();
		this.computeStateValues();
	}
	private void computeStateValues()
	{
		this.stateValue = new HashMap<State,Double>();
		State[] ss = this.getStateSpace();
		for (State s : ss)
		{
			this.stateValue.put(s, this.getBestActionValue(s));
		}
	}
	private void computeActionValues()
	{
		//Initialize
		State[] ss = this.getStateSpace();
		actionValue = new HashMap<StateActionPair,Double>();
		for (State s : ss)
		{
			Action[] as = this.getPossibleActions(s);
			for (Action a : as)
			{
				actionValue.put(new StateActionPair(s,a), 0.0);
			}
		}
		//Compute
		double delta = 0;
		do
		{
			delta = 0;
			for (State s : ss)
			{
				Action[] as = this.getPossibleActions(s); //Action space
				for (Action a : as)
				{
					StateActionPair sap = new StateActionPair(s,a);
					double prevV = this.actionValue.get(sap);
					
					TransitionProbability[] tProbs = this.getTransitionProbabilities(s, a);
					double tempV = 0;
					for (TransitionProbability tp : tProbs)
					{
						tempV += tp.probability*(this.getReward(s,a,tp.state) + gamma*this.getBestActionValue(tp.state));
					}
					this.actionValue.put(sap, tempV);
					
					delta = Math.max(delta, Math.abs(prevV - this.actionValue.get(sap)));
				}
			}
		} while (delta > theta);
	}
	//Helper functions for optimal value computation
	private double getBestActionValue(State s)
	{
		Action[] actions = this.getPossibleActions(s);
		double bestActionValue = -Double.MAX_VALUE;
		for (Action a : actions)
		{
			if (bestActionValue < this.actionValue.get(new StateActionPair(s,a)))
			{
				//Replace best action value with that of action a
				bestActionValue = this.actionValue.get(new StateActionPair(s,a));
			}
		}
		return bestActionValue;
	}
	//Values under a fixed policy
	public HashMap<State,Double> computeStateValues(Policy pi) //TODO: Verify correctness
	{
		//Declare/Initialise variables
		HashMap<State,Double> val = new HashMap<State,Double>();
		State[] ss = this.getStateSpace();
		for (State s : ss)
		{
			val.put(s,0.0);
		}
		
		double delta;
		TransitionProbability[] tProbs;
		double tempV; //Temporary value
		double prevV;
		do
		{
			delta = 0;
			for (State s : ss)
			{
				prevV = val.get(s);
				
				//Transition probabilities of all possible resulting states from following policy pi at state s
				tProbs = this.getTransitionProbabilities(s, pi.get(s));
				//Expected value
				tempV = 0;
				for (TransitionProbability tp : tProbs)
				{
					tempV += tp.probability*val.get(tp.state);
				}
				tempV *= gamma;
				tempV += this.getReward(s);
				val.put(s, tempV);
				
				delta = Math.max(delta, Math.abs(prevV - val.get(s)));
			}
		} while (delta > theta);
		
		return val;
	}
	//Loss function (for graphing purposes)
	public double computeLoss(Policy policy)
	{
		State[] ss = this.getStateSpace();
		HashMap<State,Double> vHat = this.computeStateValues(policy);
		double total = 0;
		for (State s : ss)
		{
			total += Math.abs(vHat.get(s) - this.stateValue.get(s));
		}
		return total/ss.length;
	}
	
	//Returns all possible states in this environment
	public abstract State[] getStateSpace();
	
	//Gives all actions that can be taken at a particular state
	public abstract Action[] getPossibleActions();
	public abstract Action[] getPossibleActions(State s);
	
	//Gives an array of all possible states that can come of taking action a at state s
	public abstract State[] getPossibleNextStates(State s, Action a);
	public abstract State[] getPossibleNextStates(StateActionPair sap);
	//Gives an outcome (stochastic) for taking a certain action
	public State getNextState(StateActionPair sap)
	{
		return getNextState(sap.state, sap.action);
	}
	public State getNextState(State s, Action a)
	{
		TransitionProbability[] tps = this.getTransitionProbabilities(s, a);
		double pSum = 0.0;
		double r = Math.random();
		for (TransitionProbability tp : tps)
		{
			pSum += tp.probability;
			if (r <= pSum)
			{
				return tp.state;
			}
		}
		return tps[tps.length-1].state;
	}
	
	//Gives an array containing all possible final states and the probability of reaching those states
	public abstract TransitionProbability[] getTransitionProbabilities(State s, Action a);
	
	//Gives the reward for a certain state or state-action pair
	public abstract double getReward(State s);
	public abstract double getReward(State s, Action a, State finalState);
	public abstract double getReward(StateActionPair sap, State finalState);
	
	//Output
	public void displayPolicy(Policy policy) { System.err.println("The displayPolicy() method was not overridden"); } //TODO: Should be overridden
	public void displayStateValues()
	{
		for (State s : this.getStateSpace())
		{
			System.out.println(s.state + "\t" + this.stateValue.get(s));
		}
	}
	public void displayActionValues() {}
}

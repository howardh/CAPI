/**
 * Only usable for environments with a finite state and action space
 */
public abstract class Environment
{
	double gamma = 0.99; //Used for value computations
	final double theta = 1e-24;	//Max error
	protected Value value;	//Optimal value function
	
	//Optimal value computation functions
	public void computeValues()
	{
		//Must be called in this order. Action values are used to calculate state values.
		this.computeActionValues();
		this.computeStateValues();
	}
	private void computeStateValues()
	{
//		value = new Value(this);
//		this.stateValue = new HashMap<State,Double>();
		State[] ss = this.getStateSpace();
		for (State s : ss)
		{
			this.value.put(s, this.getBestActionValue(s));
		}
	}
	private void computeActionValues()
	{
		//Initialize
		State[] ss = this.getStateSpace();
		value = new Value(this);
		//Compute
		double delta;
		do
		{
			delta = 0;
			for (State s : ss) for (Action a : this.getPossibleActions(s)) //For each state-action pair
			{
				StateActionPair sap = new StateActionPair(s,a);
				double prevV = this.value.get(sap);
				
				TransitionProbability[] tProbs = this.getTransitionProbabilities(s, a);
				double tempV = 0;
				for (TransitionProbability tp : tProbs)
				{
					tempV += tp.probability*(this.getReward(s,a,tp.state) + gamma*this.getBestActionValue(tp.state));
				}
				this.value.put(sap, tempV);
				
				delta = Math.max(delta, Math.abs(prevV - this.value.get(sap)));
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
			if (bestActionValue < this.value.get(new StateActionPair(s,a)))
			{
				//Replace best action value with that of action a
				bestActionValue = this.value.get(new StateActionPair(s,a));
			}
		}
		return bestActionValue;
	}
	//Values under a fixed policy
	public Value computeStateValues(Policy pi) //TODO: Verify correctness
	{
		//Declare/Initialise variables
		Value val = new Value(this);
		State[] ss = this.getStateSpace();
		for (State s : ss)
		{
			val.put(s,0.0);
		}
		
		double delta;
		do
		{
			delta = 0;
			for (State s : ss)
			{
				double prevV = val.get(s);
				
				//Transition probabilities of all possible resulting states from following policy pi at state s
				TransitionProbability[] tProbs = this.getTransitionProbabilities(s, pi.get(s));
				//Expected value
				double tempV = 0;
				for (TransitionProbability tp : tProbs)
				{
					tempV += tp.probability*( this.getReward(tp.saPair, tp.state) + gamma*val.get(tp.state) );
				}
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
		Value vHat = this.computeStateValues(policy);
		double total = 0;
		for (State s : ss)
		{
			total += Math.abs(vHat.get(s) - this.value.get(s));
		}
		return total/ss.length;
	}
	
	//Returns all possible states in this environment
	public abstract State[] getStateSpace();
	
	//Gives all actions that can be taken at a particular state
	public static Action[] getPossibleActions() { System.err.println("Environment.getPossibleActions() not overridden."); return null; }
	public abstract Action[] getPossibleActions(State s);
	
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
	public void displayPolicy(Policy policy) { System.err.println("The displayPolicy() method was not overridden"); }
	public void displayStateValues()
	{
		for (State s : this.getStateSpace())
		{
			System.out.println(s.state + "\t" + this.value.get(s));
		}
	}
	public void displayActionValues() {}
}

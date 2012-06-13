import java.util.HashMap;

public class ValueIterationAgent extends Agent
{
	State[] stateSpace;
	final double gamma = 0.99;
	final boolean ISOLATE_ITERATIONS = true; //If true, then when evaluating the state values in one iterations, all of the values used come from the previous iteration, rather than using the new values as they get updated
	
	public ValueIterationAgent(Environment e)
	{
		super(e);
		
		this.stateSpace = e.getStateSpace();
		Action[] actions;
		for (State s : stateSpace)
		{
			this.stateValue.put(s, 0.0);
			actions = env.getPossibleActions(s);
			this.policy.put(s, actions[(int)(Math.random()*Integer.MAX_VALUE)%actions.length]);
		}
	}

	public void loop()
	{
		HashMap<State,Double> prevStateValue = (HashMap<State, Double>) (this.ISOLATE_ITERATIONS ? this.stateValue.clone() : this.stateValue);
		
		for (State s : stateSpace)
		{
			double targetVal = -Double.MAX_VALUE;
			for (Action a : env.getPossibleActions(s))
			{
				TransitionProbability tProbs[] = env.getTransitionProbabilities(s, a);
				double sum = 0;
				for (TransitionProbability tp : tProbs)
				{
					sum += tp.probability*(env.getReward(tp.saPair,tp.state) + gamma*prevStateValue.get(tp.state));
				}
				targetVal = Math.max( targetVal, sum);
			}
			this.stateValue.put(s, targetVal);
		}
		
		for (State s: stateSpace)
		{
			policy.put(s, this.getGreedyAction(s));
		}
	}
	
	private Action getGreedyAction(State s)
	{
		Action[] actions = env.getPossibleActions(s);
		Action bestAction = actions[0];
		double maxValue = -Double.MAX_VALUE;
		double tempValue;
		for (Action a : actions)
		{
			TransitionProbability[] tProbs = env.getTransitionProbabilities(s, a);
			tempValue = 0;
			for (TransitionProbability tp : tProbs)
			{
				tempValue += tp.probability*(env.getReward(tp.saPair,tp.state) + gamma*this.stateValue.get(tp.state));
			}
			
			if (maxValue <= tempValue)
			{
				maxValue = tempValue;
				bestAction = a;
			}
		}
		return bestAction;
	}
	
	public void displayPolicy()
	{
		for (State s : stateSpace)
		{
			System.out.print(policy.get(s).action.equals("-1")?"<":">");
		}
		System.out.println();
	}
}

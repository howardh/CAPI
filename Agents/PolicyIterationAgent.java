

public class PolicyIterationAgent extends Agent
{
	State[] stateSpace;
	final double GAMMA = 0.99;

	public PolicyIterationAgent(Environment e)
	{
		super(e);
		
		this.stateSpace = e.getStateSpace();
		Action[] actions;
		for (State s : stateSpace)
		{
			this.stateValue.put(s, 0.0);
			actions = env.getPossibleActions(s);
			//this.policy.put(s, actions[(int)(Math.random()*Integer.MAX_VALUE)%actions.length]);
			//this.policy.put(s, actions[0]);
			this.policy.put(s, actions.length==2?actions[1]:actions[0]);
		}
	}

	public void loop()
	{
		this.policyEvaluation();
		this.policyImprovement();
	}

	private void policyEvaluation()
	{
		this.stateValue = env.computeStateValues(this.policy);
	}

	private void policyImprovement()
	{
		for (State s : stateSpace)
		{
			Action[] as = env.getPossibleActions(s);
			Action bestAction = null;
			double bestActionValue = -Double.MAX_VALUE;
			for (Action a : as)
			{
				TransitionProbability[] tProbs = env.getTransitionProbabilities(s, a);
				double total = 0;
				for (TransitionProbability tp : tProbs)
				{
					total += tp.probability*(env.getReward(tp.saPair,tp.state)+GAMMA*this.stateValue.get(tp.state));
				}
				
				if (total >= bestActionValue)
				{
					bestActionValue = total;
					bestAction = a;
				}
			}
			
			this.policy.put(s,bestAction);
		}
	}
	
	public void displayPolicy()
	{
		env.displayPolicy(this.policy);
	}
}

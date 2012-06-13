/*
 * Let pi = a random policy
 * Let K = the number of iterations (a constant)
 * Loop k times
 * 		Evaluate the current policy (calculateActionValue)
 * 		{
 * 			while the largest change is not small enough (delta > theta)
 * 				foreach s and a
 * 					Q(s,a) = Reward(s,a,s+a) + gamma*Q(s+a,pi[s+a])
 * 		}
 * 		Construct a greedy policy (constructGreedyPolicy)
 * 		{
 * 			foreach s
 * 				pi[s] = argmax_a {Q(s,a)}
 * 		}
 * 		Improve the policy (constructPolicy)
 * 		{
 * 			double minLoss = Double.MAX_VALUE	//Loss of the policy with minimum loss
 * 			int[] minPi							//Policy with minimum loss (with loss minLoss)
 * 			foreach pi
 * 				if loss(pi) < minLoss
 * 					minLoss = loss(pi)
 * 					minPi = pi
 * 			policy = minPi
 * 		}
 */

import java.util.HashMap;

public class CAPIAgent extends Agent
{
	State[] stateSpace;
	final double gamma = 0.99;
	final double theta = 0.00000001;
	
	protected HashMap<State,Action> gPolicy; //Greedy policy
	
	public CAPIAgent(Environment e)
	{
		super(e);
		
		gPolicy = new HashMap<State,Action>();
		
		this.stateSpace = e.getStateSpace();
		Action[] actions;
		for (State s : stateSpace)
		{
			//TODO: Initialize Action values
			actions = env.getPossibleActions();
			this.stateValue.put(s, 0.0);
			this.policy.put(s, actions[0]);
			this.gPolicy.put(s, actions[0]);
			for (Action a : actions)
			{
				this.actionValue.put(new StateActionPair(s, a),0.0);
			}
		}
		
		//FIXME: Debugging stuff. Remove me. (It looks like starting with a different policy doesn't change anything)
		for (int i = stateSpace.length/2; i < stateSpace.length-1; i++)
		{
			this.gPolicy.put(stateSpace[i], env.getPossibleActions()[0]);
		}
	}

	public void loop()
	{
		this.policyEvaluation();
		this.constructGreedyPolicy();
		this.constructPolicy();
	}
	
	public void policyEvaluation() //TODO: Check if it's complete
	{
		/*
		 * 			while the largest change is not small enough (delta > theta)
		 * 				foreach s and a
		 * 					Q(s,a) = Reward(s,a,s+a) + gamma*Q(s+a,pi[s+a])
		 */
		double delta;
		do
		{
			delta = 0;
			for (State s : stateSpace)
			{
				Action[] actions = env.getPossibleActions(s);
				for (Action a : actions)
				{
					StateActionPair sap = new StateActionPair(s,a);
					TransitionProbability[] tProbs = env.getTransitionProbabilities(s, a);
					double sum = 0;
					for (TransitionProbability tp : tProbs)
					{
						Action a2 = this.getAction(tp.state); //Next action, as dictated by the current policy
						sum += tp.probability*(env.getReward(sap, tp.state)+gamma*getActionValue(tp.state, a2));
					}
					
					double prevV = this.actionValue.get(sap);
					this.actionValue.put(sap, sum);
					delta = Math.max(delta, Math.abs(prevV-this.actionValue.get(sap)));
				}
			}
		} while (delta > theta);
	}
	
	public void constructGreedyPolicy()
	{
		for (State s : stateSpace)
		{
			gPolicy.put(s, this.getGreedyAction(s)); //FIXME: WHY DOES THIS GIVE A DIFFERENT RESULT WHEN STORED IN policy AND gPolicy???
		}
	}
	
	public Action getGreedyAction (State s)
	{
		Action[] actions = env.getPossibleActions(s);
		double maxVal = -Double.MAX_VALUE;
		Action bestAction = null;
		double tempVal;
		for (Action a : actions)
		{
			tempVal = this.getActionValue(s, a);
			if (maxVal <= tempVal)
			{
				maxVal = tempVal;
				bestAction = a;
			}
		}
		return bestAction;
	}
	
	public void constructPolicy() 
	{
		//TODO complete me.
		//TODO: Make this work for any environment
		//FIXME: This assumes that the provided state space is in numerical order.
		//FIXME: The tested policy space is missing two policies. They're marked below.
		Action[] a = env.getPossibleActions();
		
		HashMap<State,Action> currentPolicy = new HashMap<State,Action>();
		for (State s : stateSpace) 
		{
			currentPolicy.put(s, a[0]);
		}
		double currentLoss = Double.MAX_VALUE;
		
		HashMap<State,Action> bestPolicy = (HashMap<State, Action>) currentPolicy.clone();
		double bestLoss = Double.MAX_VALUE;
		
		for (State s : stateSpace)
		{
			currentPolicy.put(s, a[1]);
			
			currentLoss = this.getLoss(currentPolicy);
			if (currentLoss < bestLoss)
			{
				bestLoss = currentLoss;
				bestPolicy = (HashMap<State, Action>) currentPolicy.clone();
			}
		}
		
		for (State s : stateSpace)
		{
			currentPolicy.put(s, a[0]);
			currentLoss = this.getLoss(currentPolicy);
			if (currentLoss < bestLoss)
			{
				bestLoss = currentLoss;
				bestPolicy = (HashMap<State, Action>) currentPolicy.clone();
			}
		}
		
		//TODO: Missed a case here too
		
		policy = bestPolicy;
	}
	
	public double getLoss(HashMap<State,Action> pi)
	{
		double totalLoss = 0;
		for(State s : stateSpace)
		{
			totalLoss += getLoss(s,pi.get(s));
		}
		return totalLoss;
	}
	
	public double getLoss(State s, Action currentAction)
	{
		if (currentAction.equals(this.gPolicy.get(s))) return 0;
		return getActionGap(s);
	}
	
	public double getActionGap(State s)
	{
		Action[] actions = env.getPossibleActions(s);
		if (actions.length == 2)
		{
			return Math.abs(this.getActionValue(s, actions[0]) - this.getActionValue(s, actions[1]));
		}
		if (actions.length > 2)
		{
			System.err.println("Something's wrong");
			return 0;
		}
		return 0;
	}
	
	public void displayPolicy()
	{
		System.out.print("g");
		env.displayPolicy(this.gPolicy);
		System.out.print("-");
		env.displayPolicy(this.policy);
	}
	public void displayPolicy(HashMap<State,Action> pi)
	{
		for (State s : stateSpace)
		{
			System.out.print(pi.get(s).action.equals("-1")?"<":">");
		}
		System.out.println();
	}
}

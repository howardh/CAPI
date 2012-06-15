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
	final double theta = 1e-24;
	
	protected Policy gPolicy; //Greedy policy
	
	public CAPIAgent(Environment e)
	{
		super(e);
		
		gPolicy = new Policy();
		
		this.stateSpace = e.getStateSpace();
		Action[] actions;
		for (State s : stateSpace)
		{
			actions = env.getPossibleActions();
			this.stateValue.put(s, 0.0);
			this.policy.put(s, actions[0]);
			this.gPolicy.put(s, actions[0]);
			for (Action a : actions)
			{
				this.actionValue.put(new StateActionPair(s, a),0.0);
			}
		}
	}

	public void loop()
	{
		this.policyEvaluation();
		this.constructGreedyPolicy();
		this.constructPolicy();
	}
	
	public void policyEvaluation()
	{
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
			gPolicy.put(s, this.getGreedyAction(s)); 
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
	
	@SuppressWarnings("unchecked")
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
		
		Policy bestPolicy = (Policy) currentPolicy.clone();
		double bestLoss = Double.MAX_VALUE;
		
		for (State s : stateSpace)
		{
			currentPolicy.put(s, a[1]);
			
			currentLoss = this.getLoss(currentPolicy);
			if (currentLoss < bestLoss)
			{
				bestLoss = currentLoss;
				bestPolicy = (Policy) currentPolicy.clone();
			}
		}
		
		for (State s : stateSpace)
		{
			currentPolicy.put(s, a[0]);
			currentLoss = this.getLoss(currentPolicy);
			if (currentLoss < bestLoss)
			{
				bestLoss = currentLoss;
				bestPolicy = (Policy) currentPolicy.clone();
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
		return Math.abs(this.getActionValue(s, currentAction) - this.getActionValue(s, this.gPolicy.get(s)));
	}
	
	public void displayPolicy()
	{
		System.out.println("g");
		env.displayPolicy(this.gPolicy);
		System.out.println("-");
		env.displayPolicy(this.policy);
	}
	public void displayPolicy(Policy pi)
	{
		env.displayPolicy(pi);
	}
}

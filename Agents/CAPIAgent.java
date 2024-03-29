import gridworld.Action;
import gridworld.CPolicy;
import gridworld.Chain;
import gridworld.Policy;
import gridworld.State;
import parent.Environment;
import parent.StateActionPair;
import parent.TransitionProbability;

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

public class CAPIAgent extends Agent
{
	State[] stateSpace;
	final double gamma = 0.99;
	final double theta = 1e-24;
	
	//public CPolicy policy;
	protected Policy gPolicy; //Greedy policy
	
	public CAPIAgent(Environment e)
	{
		super(e);
		
		policy = new CPolicy();
		((CPolicy)policy).next();
		//((CPolicy)policy).next();
		//((CPolicy)policy).next();
		gPolicy = new Policy();
		
		this.stateSpace = e.getStateSpace();
		Action[] actions;
		for (State s : stateSpace)
		{
			actions = env.getPossibleActions(s);
			this.value.put(s, 0.0);
			this.gPolicy.put(s, actions[0]);
			for (Action a : actions)
			{
				this.value.put(new StateActionPair(s, a),0.0);
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
					
					double prevV = this.value.get(sap);
					this.value.put(sap, sum);
					delta = Math.max(delta, Math.abs(prevV-this.value.get(sap)));
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
	
	public void constructPolicy() 
	{
		Action[] a = Chain.getPossibleActions(); //FIXME: Remove reference to Gridworld here
		
		CPolicy currentPolicy = new CPolicy();
		for (State s : stateSpace) 
		{
			currentPolicy.put(s, a[0]);
		}
		double currentLoss;
		CPolicy bestPolicy = new CPolicy(currentPolicy);
		double bestLoss = this.getLoss(currentPolicy);
		
		do
		{
			currentLoss = this.getLoss(currentPolicy);
			if (currentLoss <= bestLoss)
			{
				bestLoss = currentLoss;
				bestPolicy = new CPolicy(currentPolicy);
			}
		} while (currentPolicy.next());
		
		policy = bestPolicy;
	}
	
	public double getLoss(CPolicy pi)
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
		System.out.print("g");
		env.displayPolicy(this.gPolicy);
		System.out.print("-");
		env.displayPolicy(this.policy);
	}
	public void displayPolicy(Policy pi)
	{
		env.displayPolicy(pi);
	}
}

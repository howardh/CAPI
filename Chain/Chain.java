
import java.util.*;

public class Chain extends Environment
{
	State[] stateSpace;
	double gamma = 0.99;
	double alpha = 0.1;
	
	public Chain()
	{
		this.stateSpace = this.getStateSpace();
	}
	
	/**
	 * Updates "stateValue" to contain an approximation of the state value under the optimal policy
	 */
	public void computeStateValues()
	{
		double delta;
		double theta = 0.000000001;
		double prevV;
		do
		{
			delta = 0;
			for (State s : stateSpace)
			{
				prevV = this.stateValue.get(s);
				this.stateValue.put(s, prevV*(1-alpha)+alpha*( this.computeStateValue(s) )); //TODO
				delta = Math.max(delta, Math.abs(prevV - this.stateValue.get(s)));
			}
		} while (delta > theta);
	}
	
	/**
	 * 
	 * @param s
	 * 		The state whose value is to be computed
	 * @return
	 * 		The value of the given state using the current approximation of the state values
	 */
	public double computeStateValue(State s)
	{
		Action actions[] = this.getPossibleActions(s);
		if (actions.length == 2)
		{
			return this.getReward(s)+gamma*Math.max( this.stateValue.get(this.getNextState(s, actions[0])),
					                                 this.stateValue.get(this.getNextState(s, actions[1])));
		}
		if (actions.length == 1)
		{
			return this.getReward(s)+gamma*this.stateValue.get(this.getNextState(s, actions[0]));
		}
		System.out.println("SOMETHING IS WRONG");
		return 0;
	}
	
	/**
	 * Outputs the values of each state (computed using "computeStateValues()") through standard output.
	 */
	public void displayStateValues()
	{
		for (State s : stateSpace)
		{
			System.out.println(s.state + "\t" + this.stateValue.get(s));
		}
	}
	public void displayActionValues()
	{
		Action[] as;
		for (State s : stateSpace)
		{
			as = this.getPossibleActions(s);
			System.out.print(s.state);
			for (Action a : as)
			{
				System.out.print("\t" + this.actionValue.get(new StateActionPair(s,a)));
			}
			System.out.println();
		}
	}
	
	public State[] getStateSpace()
	{
		State[] result = new State[200];
		for (int i = 0; i < 200; i++)
		{
			result[i] = new State((i+1)+"");
		}
		return result;
	}
	
	public Action[] getPossibleActions()
	{
		Action[] result = {new Action("1"), new Action("-1")};
		return result;
	}
	
	public Action[] getPossibleActions(State s)
	{
		if (s.state.equals("1"))
		{
			Action[] result = {new Action("1")};
			return result;
		}
		if (s.state.equals("200"))
		{
			Action[] result = {new Action("-1")};
			return result;
		}
		Action[] result = {new Action("-1"),new Action("1")};
		return result;
	}

	public State[] getPossibleNextStates(State s, Action a) {
		return null; //TODO
	}
	public State[] getPossibleNextStates(StateActionPair sap) {
		return this.getPossibleNextStates(sap.state, sap.action);
	}

	public TransitionProbability[] getTransitionProbabilities(State s, Action a)
	{
		if (s.state.equals("1"))
		{
			TransitionProbability[] result = new TransitionProbability[1];
			result[0] = new TransitionProbability();
			result[0].probability = 1;
			result[0].saPair = new StateActionPair(s,a);
			result[0].state = new State("2");
			return result;
		}
		if (s.state.equals("200"))
		{
			TransitionProbability[] result = new TransitionProbability[1];
			result[0] = new TransitionProbability();
			result[0].probability = 1;
			result[0].saPair = new StateActionPair(s,a);
			result[0].state = new State("199");
			return result;
		}
		TransitionProbability[] result = new TransitionProbability[2];
		
		result[0] = new TransitionProbability();
		result[0].probability = 0.6;
		result[0].saPair = new StateActionPair(s,a);
		result[0].state = new State((Integer.parseInt(s.state)+Integer.parseInt(a.action))+"");
		
		result[1] = new TransitionProbability();
		result[1].probability = 0.4;
		result[1].saPair = new StateActionPair(s,a);
		result[1].state = new State((Integer.parseInt(s.state)-Integer.parseInt(a.action))+"");
		return result;
	}

	public double getReward(State s)
	{
		int state = Integer.parseInt(s.state);
		if (state >= 10 && state <= 15)
			return 1.0;
		if (state >= 180 && state <= 190)
			return 0.1;
		return 0;
	}

	public double getReward(State s, Action a, State finalState)
	{
		return getReward(finalState);
	}

	public double getReward(StateActionPair sap, State finalState)
	{
		return this.getReward(sap.state, sap.action, finalState);
	}
	
	private boolean isLeft(Action a)
	{
		return a.action.equals("-1");
	}
	
	private boolean isRight(Action a)
	{
		return a.action.equals("1");
	}
	
	public void displayPolicy(HashMap<State,Action> policy)
	{
		for (State s : stateSpace)
		{
			System.out.print(policy.get(s).action.equals("-1")?"<":">");
		}
		System.out.println();
	}
}

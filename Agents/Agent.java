import java.util.*;


public abstract class Agent
{
	protected HashMap<StateActionPair,Double> actionValue;	//Q
	protected HashMap<State,Double> stateValue;		//V
	protected Policy policy;			//pi //TODO: This assumes that the policy is deterministic
	protected Environment env;
	
	public Agent(Environment e)
	{
		env = e;
		actionValue = new HashMap<StateActionPair,Double>();
		stateValue = new HashMap<State,Double>();
		policy = new Policy();
	}
	
	public abstract void loop();
	
	protected double getActionValue(State s, Action a) { return actionValue.get(new StateActionPair(s,a)); }
	protected double getActionValue(StateActionPair sap) { return actionValue.get(actionValue); }
	
	double getStateValue(State s) { return stateValue.get(s); }
	
	Action getAction(State s) { return policy.get(s); }
	
	void displayActionValues()
	{
		for (State s : env.getStateSpace())
		{
			Action[] as = env.getPossibleActions(s);
			System.out.print(s.state);
			for (Action a : as)
			{
				System.out.print("\t" + this.getActionValue(s, a));
			}
			System.out.println();
		}
	}
	void displayStateValues()
	{
		for (State s : env.getStateSpace())
		{
			System.out.println(s.state + "\t" + this.getStateValue(s));
		}
	}
	void displayPolicy(){}
}

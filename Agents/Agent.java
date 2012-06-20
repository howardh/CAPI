import gridworld.Action;
import gridworld.Policy;
import gridworld.State;
import parent.Environment;
import parent.StateActionPair;
import parent.Value;

public abstract class Agent
{
	protected Value value;
	protected Policy policy;			//pi //TODO: This assumes that the policy is deterministic
	protected Environment env;
	
	public Agent(Environment e)
	{
		env = e;
		value = new Value(e);
		policy = new Policy();
	}
	
	public abstract void loop();
	
	protected double getActionValue(State s, Action a) { return value.get(new StateActionPair(s,a)); }
	protected double getActionValue(StateActionPair sap) { return this.getActionValue(sap.state, sap.action); }
	protected double getStateValue(State s) { return value.get(s); }
	
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

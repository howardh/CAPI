package parent;
import gridworld.Action;
import gridworld.State;

public class StateActionPair
{
	public StateActionPair() {}
	public StateActionPair(State s, Action a)
	{
		state = s;
		action = a;
	}
	
	public Action action;
	public State state;
	
	public boolean equals(Object sap)
	{
		return ((StateActionPair)sap).state.equals(state) && ((StateActionPair)sap).action.equals(action);
	}
	public int hashCode() { return state.hashCode()^action.hashCode(); }
}



public class State
{
	public State(){}
	public State(String s)
	{
		state = s;
	}
	
	public String state;
	
	public boolean equals(Object s) { return ((State)s).state.equals(state); }
	public int hashCode() { return state.hashCode(); }
}

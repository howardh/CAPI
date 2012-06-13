

public class Action
{
	public Action(){}
	public Action(String a)
	{
		action = a;
	}
	
	public String action;
	
	public boolean equals(Object a) { return ((Action)a).action.equals(action); }
	public int hashCode() { return action.hashCode(); }
}

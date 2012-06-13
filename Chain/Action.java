

public class Action
{
	static public final int UP = 1,
							DOWN = 2, 
							LEFT = 3,
							RIGHT = 4;
	
	public Action(){}
	public Action(int d)
	{
		action = d;
	}
	
	public int action;
	
	public boolean equals(Object a) { return ((Action)a).action == this.action; }
	public int hashCode() { return action; }
}

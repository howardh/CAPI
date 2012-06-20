package gridworld;



public class Action
{
	static public final int UP = 0,
							DOWN = 1, 
							LEFT = 2,
							RIGHT = 3;
	
	public Action(){}
	public Action(int d)
	{
		action = d;
	}
	
	public int action;
	
	public boolean equals(Object a) { return ((Action)a).action == this.action; }
	public int hashCode() { return action; }
}



public class State
{
	public State(){}
	public State(int x, int y)
	{
		this.x = x;
		this.y = y;
		state = "("+x+","+y+")";
	}
	
	public int x;
	public int y;
	public String state;
	
	public boolean equals(Object s) { return ((State)s).x == this.x && ((State)s).y == this.y; }
	public int hashCode() { return x^y; }
}

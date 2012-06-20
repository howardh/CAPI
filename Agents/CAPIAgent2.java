import gridworld.Action;
import gridworld.State;
import parent.Environment;



public class CAPIAgent2 extends CAPIAgent
{
	public CAPIAgent2(Environment e) {super(e);}
	
	public double getLoss(State s, Action currentAction)
	{
		if (currentAction.equals(this.gPolicy.get(s))) return 0;
		return 1;
	}
}

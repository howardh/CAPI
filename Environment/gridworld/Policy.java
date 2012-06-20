package gridworld;


public class Policy
{
	Action[][] policy;
	
	public Policy()
	{
		policy = new Action[Chain.HEIGHT][Chain.WIDTH];
	}
	public Action get(State s) { return policy[s.y][s.x]; }
	public void put(State s, Action a) { policy[s.y][s.x] = a; }
}

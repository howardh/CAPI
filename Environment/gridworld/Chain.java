package gridworld;
import parent.Environment;
import parent.StateActionPair;
import parent.TransitionProbability;

public class Chain extends Environment
{
	State[] stateSpace;
	final double gamma = 0.99;
	final double alpha = 1;
	final double theta = 1e-24;
	
	/**
	 * ' ' = empty space
	 * 'x' = wall/obstacle
	 */
	final static char[][] world = {{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',}};
	final static double[][] reward = {{0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
									0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0,0,0,0,0,0,0,0,0,0,}};
	final static int WIDTH = world[0].length;	
	final static int HEIGHT = world.length;	
	
	public Chain()
	{
		this.stateSpace = this.getStateSpace();
	}
	
	private char getSquare(int x, int y)
	{
		try
		{
			return world[y][x];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return 'x';
		}
	}
	
	/**
	 * Outputs the values of each state (computed using "computeStateValues()") through standard output.
	 */
	public void displayStateValues()
	{
		for (State s : stateSpace)
		{
			System.out.println(s.state + "\t" + this.value.get(s));
		}
	}
	public void displayActionValues()
	{
		Action[] as;
		for (State s : stateSpace)
		{
			as = this.getPossibleActions(s);
			System.out.print(s.state);
			for (Action a : as)
			{
				System.out.print("\t" + this.value.get(new StateActionPair(s,a)));
			}
			System.out.println();
		}
	}
	
	public State[] getStateSpace()
	{
		State[] result = new State[world.length*world[0].length];
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				result[y*world[y].length+x] = new State(x,y);
			}
		}
		return result;
	}
	
	public static Action[] getPossibleActions()
	{
		Action[] result = {new Action(Action.LEFT), new Action(Action.RIGHT)};
		return result;
	}
	
	public Action[] getPossibleActions(State s) //TODO
	{
		return Chain.getPossibleActions();
//		Vector<Action> result = new Vector<Action>();
//		
//		if (this.getSquare(s.x-1, s.y) == ' ') result.add(new Action(Action.LEFT));
//		if (this.getSquare(s.x+1, s.y) == ' ') result.add(new Action(Action.RIGHT));
//		if (this.getSquare(s.x, s.y-1) == ' ') result.add(new Action(Action.UP));
//		if (this.getSquare(s.x, s.y+1) == ' ') result.add(new Action(Action.DOWN));
//		
//		return (Action[])(result.toArray(new Action[result.size()]));
	}

	public State[] getPossibleNextStates(State s, Action a) {
		return null; //TODO
	}
	public State[] getPossibleNextStates(StateActionPair sap) {
		return this.getPossibleNextStates(sap.state, sap.action);
	}

	public TransitionProbability[] getTransitionProbabilities(State s, Action a) //TODO
	{
		TransitionProbability[] result = new TransitionProbability[3];
		
		//Left
		result[0] = new TransitionProbability();
		result[0].probability = 0;
		result[0].saPair = new StateActionPair(s,a);
		result[0].state = new State(s.x-1,s.y);

		//Right
		result[1] = new TransitionProbability();
		result[1].probability = 0;
		result[1].saPair = new StateActionPair(s,a);
		result[1].state = new State(s.x+1,s.y);

		//No movement
		result[2] = new TransitionProbability();
		result[2].probability = 0;
		result[2].saPair = new StateActionPair(s,a);
		result[2].state = new State(s.x,s.y);
		
		//Increase probability of success
		if (a.action == Action.LEFT)
		{
			if (s.x == 0)
			{
				result[0].probability = 1;
				result[1].probability = 0;
			}
			else if (s.x == WIDTH-1)
			{
				result[0].probability = 0.9;
				result[1].probability = 0.1;
			}
			else
			{
				result[0].probability = 0.7;
				result[1].probability = 0.2;
				result[2].probability = 0.1;
			}
		}
		else
		{
			if (s.x == 0)
			{
				result[0].probability = 0;
				result[1].probability = 1;
			}
			else if (s.x == WIDTH-1)
			{
				result[0].probability = 0.1;
				result[1].probability = 0.9;
			}
			else
			{
				result[1].probability = 0.7;
				result[0].probability = 0.2;
				result[2].probability = 0.1;
			}
		}
		
		//Check for obstacles
		final int delta[][] = {{-1,0},{1,0}};
		for (int i = 0; i < 2; i++)
		{
			if (this.getSquare(s.x+delta[i][0], s.y+delta[i][1]) == 'x')
			{
				//No movement
				result[i].state = new State(s.x,s.y);
			}
		}
		
		//Normalize
		double totalProb = 0;
		for (TransitionProbability tp : result)
		{
			totalProb += tp.probability;
		}
		for (TransitionProbability tp : result)
		{
			tp.probability /= totalProb;
		}
		
		return result;
	}

	public double getReward(State s)
	{
		return reward[s.y][s.x];
	}

	public double getReward(State s, Action a, State finalState)
	{
		return getReward(s);
	}

	public double getReward(StateActionPair sap, State finalState)
	{
		return this.getReward(sap.state, sap.action, finalState);
	}
	
	public void displayPolicy(Policy policy) 
	{
//		for (int y = 0; y < world.length; y++)
//		{
//			for (int x = 0; x < world[y].length; x++)
//			{
//				switch (policy.get(new State(x,y)).action)
//				{
//					case Action.UP: 	System.out.print("^"); break;
//					case Action.DOWN: 	System.out.print("v"); break;
//					case Action.LEFT: 	System.out.print("<"); break;
//					case Action.RIGHT: 	System.out.print(">"); break;
//				}
//			}
//			System.out.println();
//		}
		//System.out.println();
		for (State s : this.getStateSpace())
		{
			System.out.print(s.state + " ");
			switch (policy.get(s).action)
			{
				case Action.LEFT: 	System.out.print("1"); break;
				case Action.RIGHT: 	System.out.print("2"); break;
			}
		}
		System.out.println();
	}
	
	public void displayEnvironment()
	{
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				System.out.print(world[y][x]);
			}
			System.out.println();
		}
		System.out.println();
	}
}

package gridworld;
import parent.Environment;
import parent.StateActionPair;
import parent.TransitionProbability;

public class Gridworld extends Environment
{
	State[] stateSpace;
	final double gamma = 0.99;
	final double alpha = 1;
	final double theta = 1e-24;
	
	/**
	 * ' ' = empty space
	 * 'x' = wall/obstacle
	 */
	final static char[][] world =
		{
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
		};
	final static double[][] reward =
		{
			{0,3,0,0,0,0,1},
			{3,0,0,0,0,0,1},
			{0,3,0,0,0,0,1},
			{3,0,0,0,0,0,1},
			{0,3,0,0,0,0,1},
			{3,0,0,0,0,0,1},
			{0,3,0,0,0,0,1},
			{3,0,0,0,0,0,1},
			{0,3,0,0,0,0,1},
		};
	
//	final static char[][] world = {{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',
//		' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',}};
//	final static double[][] reward = {{0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
//									0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0,0,0,0,0,0,0,0,0,0,}};
	final static int WIDTH = world[0].length;	
	final static int HEIGHT = world.length;	
	
	public Gridworld()
	{
		super();
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
		Action[] result = {new Action(Action.UP), new Action(Action.DOWN), new Action(Action.LEFT), new Action(Action.RIGHT)};
		return result;
	}
	
	public Action[] getPossibleActions(State s)
	{
		return Gridworld.getPossibleActions();
//		Vector<Action> result = new Vector<Action>();
//		
//		if (this.getSquare(s.x-1, s.y) == ' ') result.add(new Action(Action.LEFT));
//		if (this.getSquare(s.x+1, s.y) == ' ') result.add(new Action(Action.RIGHT));
//		if (this.getSquare(s.x, s.y-1) == ' ') result.add(new Action(Action.UP));
//		if (this.getSquare(s.x, s.y+1) == ' ') result.add(new Action(Action.DOWN));
//		
//		return (Action[])(result.toArray(new Action[result.size()]));
	}

	public TransitionProbability[] getTransitionProbabilities(State s, Action a)
	{
		TransitionProbability[] result = new TransitionProbability[4];
		
		//Up
		result[0] = new TransitionProbability();
		result[0].probability = 0;
		result[0].saPair = new StateActionPair(s,a);
		result[0].state = new State(s.x,s.y-1);
		
		//Down
		result[1] = new TransitionProbability();
		result[1].probability = 0;
		result[1].saPair = new StateActionPair(s,a);
		result[1].state = new State(s.x,s.y+1);
		
		//Left
		result[2] = new TransitionProbability();
		result[2].probability = 0;
		result[2].saPair = new StateActionPair(s,a);
		result[2].state = new State(s.x-1,s.y);
		
		//Right
		result[3] = new TransitionProbability();
		result[3].probability = 0;
		result[3].saPair = new StateActionPair(s,a);
		result[3].state = new State(s.x+1,s.y);
		
		//Increase probability of success
		result[a.action].probability += 2;
		
		//Check for obstacles
		final int delta[][] = {{0,-1},{0,1},{-1,0},{1,0}};
		for (int i = 0; i < 4; i++)
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
		for (int y = 0; y < world.length; y++)
		{
			for (int x = 0; x < world[y].length; x++)
			{
				switch (policy.get(new State(x,y)).action)
				{
					case Action.UP: 	System.out.print("^"); break;
					case Action.DOWN: 	System.out.print("v"); break;
					case Action.LEFT: 	System.out.print("<"); break;
					case Action.RIGHT: 	System.out.print(">"); break;
				}
			}
			System.out.println();
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

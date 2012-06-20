package parent;
import gridworld.State;

public class TransitionProbability
{
	public StateActionPair saPair;	//Starting condition
	public State state;				//final state
	//Probability of reaching final state from the given initial conditions
	public double probability; 
}

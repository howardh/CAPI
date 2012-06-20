package parent;

import gridworld.Action;
import gridworld.State;

import java.util.*;

public class Value
{
	private HashMap<StateActionPair,Double> actionValue;	//Optimal Q
	private HashMap<State,Double> stateValue;				//Optimal V
	
	public Value(Environment e)
	{
		actionValue = new HashMap<StateActionPair,Double>();
		stateValue = new HashMap<State,Double>();
		
		for (State s : e.getStateSpace())
		{
			stateValue.put(s, 0.0);
			for (Action a : e.getPossibleActions(s))
			{
				actionValue.put(new StateActionPair(s,a), 0.0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Value(Value v)
	{
		this.actionValue = (HashMap<StateActionPair, Double>) v.actionValue.clone();
		this.stateValue = (HashMap<State, Double>) v.stateValue.clone();
	}
	
	public double get(StateActionPair sap) { return actionValue.get(sap); }
	public double get(State s, Action a) { return actionValue.get(new StateActionPair(s,a)); }
	public double get(State s) { return stateValue.get(s); }
	
	public void put(StateActionPair sap, double v) { actionValue.put(sap,v); }
	public void put(State s, Action a, double v) { actionValue.put(new StateActionPair(s,a),v); }
	public void put(State s, double v) { stateValue.put(s,v); }
}

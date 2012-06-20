package gridworld;

/**
 * CAPI policy
 */
public class CPolicy extends Policy implements Cloneable
{
	private final int X = 0;
	private final int Y = 1;
	private final int attributeCount = 2;
	
	private int action;			//Contains an action index if it is a leaf, otherwise, contains -1
	private int attribute;		//The attribute of the state we're concerned with
	private int attVal; 		//Attribute value at which the partition is made
	private Action[] actions;	//All possible actions
	private CPolicy less; 		//Policy to follow if the value of the attribute is < attVal
	private CPolicy greater;	//Policy to follow if the value of the attribute is >= attVal
	private int width;
	private int height;
	
	public CPolicy()
	{
		this(false);
		less = new CPolicy(true);
		greater = new CPolicy(true);
		width = Chain.WIDTH;
		height = Chain.HEIGHT;
	}
	
	public CPolicy(boolean leaf)
	{
		actions = Chain.getPossibleActions();
		action = leaf ? 0 : -1;
	}
	
	public CPolicy(CPolicy p)
	{
		this.action = p.action;
		this.actions = Chain.getPossibleActions();	//This array shouldn't change, so this should be okay without cloning
		if (this.action == -1)
		{
			this.attribute = p.attribute;
			this.attVal = p.attVal;
			this.less = new CPolicy(p.less);
			this.greater = new CPolicy(p.greater);
			this.width = p.width;
			this.height = p.height;
		}
	}
	
	public Action get(State s)
	{
		if (this.action != -1) return this.actions[action];
		switch(attribute)
		{
			case X: return (s.x < attVal) ? less.get(s) : greater.get(s);
			case Y: return (s.y < attVal) ? less.get(s) : greater.get(s);
		}
		System.err.println("CPolicy.get(s) error");
		return null;
	}
	
	/**
	 * Gets the next policy.
	 * Starts by splitting horizontally and for each possible split, try all possible action combinations before trying the next split
	 * @return
	 * 		true - if there exists a next policy
	 * 		false - otherwise
	 */
	public boolean next() //TODO: verify correctness
	{
		//If this node is a leaf
		if (action != -1)
		{
			if (++action >= actions.length)
			{
				return false;
			}
			return true;
		}
		
		//If this node is not a leaf
		if (this.attribute == X)
		{
			//If we've looked through all possible partitions
			if (this.attVal == width)
			{
				this.reset();
				this.attribute = Y;
				return true;
			}
			
			if (!greater.next())
			{
				greater.reset();
				if (!less.next())
				{
					less.reset();
					++attVal;
					return this.next();
				}
			}
			return true;
		}
		else
		{
			//If we've looked through all possible partitions
			if (this.attVal == height)
			{
				return false;
			}
			
			if (!greater.next())
			{
				greater.reset();
				if (!less.next())
				{
					less.reset();
					++attVal;
					return this.next();
				}
			}
			return true;
		}
	}
	
	public void reset()
	{
		if (action == -1)
		{
			less.reset();
			greater.reset();
			attVal = 0;
			return;
		}
		
		action = 0;
	}
}

import gridworld.Chain;
import gridworld.Gridworld;
import gridworld.State;
import parent.Environment;
import parent.Value;

public class Main
{
	public static void main(String args[])
	{
		//Gridworld env = new Gridworld();
		Chain env = new Chain();
		//env.displayEnvironment();
		env.computeValues();
		//env.displayActionValues();
		//env.displayStateValues();
		
		//all(env);
		//valueIteration(env);
		//policyIteration(env);
		//capi(env);
		//capi2(env);
		
		ValueIterationAgent via = new ValueIterationAgent(env);
		Value v = env.computeStateValues(via.policy);
		for (State s : env.getStateSpace())
		{
			System.out.println(s.state + " " + v.get(s));
		}
	}
	
	static void policyIteration(Environment env)
	{
		PolicyIterationAgent pia = new PolicyIterationAgent(env);
		//via.displayStateValue();
		for (int i = 0; i < 30; i++)
		{
			//pia.displayPolicy();
			pia.loop();
			System.out.println(i + " " + env.computeLoss(pia.policy));
		}
		pia.displayPolicy();
		//pia.displayStateValue();
	}
	
	static void valueIteration(Environment env)
	{
		ValueIterationAgent via = new ValueIterationAgent(env);
		//via.displayStateValue();
		for (int i = 0; i < 30; i++)
		{
			//via.displayPolicy();
			via.loop();
			System.out.println(i + " " + env.computeLoss(via.policy));
		}
		//via.displayPolicy();
		//via.displayStateValues();
	}
	
	static void capi(Environment env)
	{
		CAPIAgent ca = new CAPIAgent(env);
		for (int i = 0; i < 30; i++)
		{
			ca.displayPolicy();
			ca.loop();
			//ca.displayPolicy();
			//System.out.println(i + " " + env.computeLoss(ca.policy));
		}
		//ca.displayActionValues();
	}
	
	static void capi2(Environment env)
	{
		CAPIAgent2 ca = new CAPIAgent2(env);
		//ca.env.displayPolicy(ca.policy);
		for (int i = 0; i < 30; i++)
		{
			ca.loop();
			ca.displayPolicy();
			//System.out.println(i + " " + env.computeLoss(ca.policy));
		}
	}
	
	static void all(Environment env)
	{
		ValueIterationAgent via = new ValueIterationAgent(env);
		PolicyIterationAgent pia = new PolicyIterationAgent(env);
		CAPIAgent ca = new CAPIAgent(env);
		CAPIAgent2 ca2 = new CAPIAgent2(env);
		
		for (int i = 0; i < 30; i++)
		{
			System.out.println(i + " " + env.computeLoss(via.policy)
									+ " " + env.computeLoss(pia.policy)
									+ " " + env.computeLoss(ca.policy)
									+ " " + env.computeLoss(ca2.policy));
			via.loop();
			pia.loop();
			ca.loop();
			ca2.loop();
		}
	}
}

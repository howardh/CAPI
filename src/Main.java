

public class Main
{
	public static void main(String args[])
	{
		Gridworld env = new Gridworld();
		//env.displayEnvironment();
		env.computeValues();
		//env.displayActionValues();
		//env.displayStateValues();
		
		//valueIteration(env);
		policyIteration(env);
		//capi(env);
		//capi2(env);
		
//		CPolicy p = new CPolicy();
//		do
//		{
//			env.displayPolicy(p);
//		} while (p.next());
	}
	
	static void policyIteration(Environment env)
	{
		PolicyIterationAgent pia = new PolicyIterationAgent(env);
		//via.displayStateValue();
		for (int i = 0; i < 300; i++)
		{
			pia.displayPolicy();
			pia.loop();
			//System.out.println(i + " " + env.computeLoss(pia.policy));
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
		for (int i = 0; i < 5; i++)
		{
			//ca.displayPolicy();
			ca.loop();
			//ca.displayPolicy();
			System.out.println(i + " " + env.computeLoss(ca.policy));
		}
		ca.displayActionValues();
	}
	
	static void capi2(Environment env)
	{
		CAPIAgent2 ca = new CAPIAgent2(env);
		//ca.env.displayPolicy(ca.policy);
		for (int i = 0; i < 30; i++)
		{
			ca.loop();
			//ca.displayPolicy();
			System.out.println(i + " " + env.computeLoss(ca.policy));
		}
	}
}

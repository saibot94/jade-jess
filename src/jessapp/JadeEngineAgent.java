package jessapp;

import jade.core.Agent;

public class JadeEngineAgent extends JessAgentBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {

		addBehaviour(new JessBehaviour(this, "jessapp.clp"));

		printStatus("started up!");
	}

}

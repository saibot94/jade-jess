package jessapp;

import jade.core.Agent;

public class JessEngineAgent extends JessAgentBase {
	private JessBehaviour jessBehaviour;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		registerAsType("jess-engine", "jess");
		jessBehaviour = new JessBehaviour(this, "jessjade.clp");

		addBehaviour(jessBehaviour);
		addBehaviour(new JessMessageListener(this, jessBehaviour));
		printStatus("started up!");
	}

}

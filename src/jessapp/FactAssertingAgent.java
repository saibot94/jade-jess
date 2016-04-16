package jessapp;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class FactAssertingAgent extends JessAgentBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				
			}
		});
	}
}

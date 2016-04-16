package hello;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class HelloAgent extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	protected void setup() {
		addBehaviour(new OneShotBehaviour() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -6014705827081602345L;

			public void action() {
				System.out.println("Hello, world!");
			}
		});
	}

}

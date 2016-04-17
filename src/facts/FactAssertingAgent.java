package facts;

import jess.JessAgentBase;

public class FactAssertingAgent extends JessAgentBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		addBehaviour(new FactSendingBehaviour(this, 1));

        printStatus("started up!");
    }
}

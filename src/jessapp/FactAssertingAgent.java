package jessapp;

public class FactAssertingAgent extends JessAgentBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		addBehaviour(new FactSendingBehaviour(this, "(product (name \"Potato\") (type veg))"));

        printStatus("started up!");
    }
}

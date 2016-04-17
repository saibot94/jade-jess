package jessapp;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class JessMessageListener extends CyclicBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JessBehaviour jessBehaviour;


	public JessMessageListener(Agent agent, JessBehaviour jessBehaviour){
		super(agent);
		this.jessBehaviour = jessBehaviour;
	}
	@Override
	public void action() {
		try{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if(msg != null){
				if(jessBehaviour.addFact(msg.getContent())){
					System.out.println("Succesfully asserted a fact!");
				}
				else{
					System.err.println("Error asserting the fact!");
				}
			}
			else{
				block();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}

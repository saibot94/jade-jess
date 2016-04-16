package book;

import java.util.HashMap;
import java.util.Map;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookSellerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BookSellerGui myGui;
	private Map<String, Integer> catalogue;

	protected void setup() {
		catalogue = new HashMap<String, Integer>();
		myGui = new BookSellerGui(this);
		myGui.setVisible(true);

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new OfferRequestsServer());

		addBehaviour(new PurchaseOffersBehaviour());
	}

	public void updateCatalogue(String title, int price) {
		catalogue.put(title, price);
	}

	@Override
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		myGui.dispose();
		System.out.println("Agent " + getAID().getName() + " is going down!");
	}

	private class OfferRequestsServer extends CyclicBehaviour {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.CFP);

			ACLMessage msg = myAgent.receive(mt);

			if (msg != null) {
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = (Integer) catalogue.get(title);
				if (price != null) {
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price));
				} else {
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("n/a");
				}

				send(reply);
			} else {
				block();
			}
		}
	}

	private class PurchaseOffersBehaviour extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {

			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);

			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = catalogue.remove(title);
				if (price != null) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title + " sold to agent "
							+ msg.getSender().getName());
				} else {
					// The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("n/a");
				}
				send(reply);
			} else {
				block();
			}
		}
	}
}

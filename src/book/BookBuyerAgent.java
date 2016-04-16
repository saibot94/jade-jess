package book;

import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

public class BookBuyerAgent extends Agent {

	/**
	 * 
	 */
	private String targetBookTitle;
	private List<AID> sellerAgents;

	private static final long serialVersionUID = 1L;

	private void printStatus(String message) {
		System.out.println("Agent: " + getAID().getName() + " " + message);
	}

	protected void setup() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			targetBookTitle = (String) args[0];
			System.out.println("Trying to buy: " + targetBookTitle);

			// Discovering the sellers

			printStatus("is started!");

			addBehaviour(new TickerBehaviour(this, 10000) {
				protected void onTick() {
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("book-selling");
					template.addServices(sd);
					sellerAgents = new ArrayList<AID>();

					try {
						DFAgentDescription[] result = DFService.search(myAgent,
								template);
						System.out
								.println("Found the following seller agents:");
						for (int i = 0; i < result.length; ++i) {
							sellerAgents.add(result[i].getName());
							System.out.println(sellerAgents.get(i).getName());
						}
					} catch (FIPAException fe) {
						fe.printStackTrace();
					}

					myAgent.addBehaviour(new RequestPerformer());
				}
			});

		} else {
			System.out.println("No book name specified");
			doDelete();
		}

	}

	protected void takeDown() {
		printStatus("is terminating!");
	}

	private class RequestPerformer extends Behaviour {
		private AID bestSeller;
		private int bestPrice;
		private int repliesCnt = 0;
		private MessageTemplate mt;
		private int step = 0;

		public void action() {
			switch (step) {
			case 0:
				sendCFPToSellers();
				break;
			case 1:
				receiveOffersFromSellers();
				break;
			case 2:
				sendPurchaseOrder();
				break;
			case 3:
				receivePurchaseReply();
				break;
			}

		}

		private void receivePurchaseReply() {
			ACLMessage reply = myAgent.receive();
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.INFORM) {
					// Purchase successful
					System.out.println("Purchase successful!");
					System.out.println("Book " + targetBookTitle + " bought!");
					System.out.println("Price = " + bestPrice);
					myAgent.doDelete();
				}
				step = 4;
			} else {
				block();
			}
		}

		private void sendPurchaseOrder() {
			ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			order.addReceiver(bestSeller);
			order.setContent(targetBookTitle);
			order.setConversationId("book-trade");
			order.setReplyWith("order" + System.currentTimeMillis());

			myAgent.send(order);

			mt = MessageTemplate.and(
					MessageTemplate.MatchConversationId("book-trade"),
					MessageTemplate.MatchInReplyTo(order.getReplyWith()));

			step = 3;
		}

		private void receiveOffersFromSellers() {
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					int price = Integer.parseInt(reply.getContent());
					if (bestSeller == null || price < bestPrice) {
						bestPrice = price;
						bestSeller = reply.getSender();
					}
				}
				repliesCnt++;
				if (repliesCnt >= sellerAgents.size()) {
					step = 2;
				}
			} else {
				block();
			}
		}

		private void sendCFPToSellers() {
			// Send a request to all the sellers
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			for (AID aid : sellerAgents) {
				msg.addReceiver(aid);
			}
			msg.setContent(targetBookTitle);
			msg.setConversationId("book-trade");
			msg.setReplyWith("cfp" + System.currentTimeMillis());
			myAgent.send(msg);

			mt = MessageTemplate.and(
					MessageTemplate.MatchConversationId("book-trade"),
					MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
			step = 1;
		}

		public boolean done() {
			return (step == 2 && bestSeller == null) || step == 4;
		}

	}

}

package jess;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class JessAgentBase extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void printStatus(String message) {
		System.out.println("Agent: " + getAID().getName() + " " + message);
	}
	
	protected void registerAsType(String type, String name){

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName(name);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			printStatus("registered on dfd");
		} catch (FIPAException fe) {
			System.err.println("ERR: Couldn't register as the specified type");
		}
	}
	
	
}

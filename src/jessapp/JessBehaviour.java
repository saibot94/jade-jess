package jessapp;

import java.io.FileReader;
import java.io.IOException;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jess.Jesp;
import jess.JessException;
import jess.Rete;

public class JessBehaviour extends JessBehaviourBase {
	// the Jess engine
	private jess.Rete jess;
	// maximum number of passes that a run of Jess can execute before giving
	// control to the agent
	private static final int MAX_JESS_PASSES = 1;

	public JessBehaviour(Agent agent, String jessFile){
		super(agent);
		Rete jess = new Rete();
		try{
			FileReader fr = new FileReader(jessFile);
			Jesp jessParser = new Jesp(fr, jess);
			try{
				jessParser.parse(false);
			}
			catch(JessException je){
				je.printStackTrace();
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			System.err.println("Error loading jess file!");
		}
	}
	
	@Override
	public void action() {
		int executedPasses = -1;
		try{
			executedPasses = jess.run(MAX_JESS_PASSES);
		}
		catch(JessException je){
			je.printStackTrace();
		}
		
		if(executedPasses < MAX_JESS_PASSES){
			block();
		}
	}
	


	boolean addFact(String jessFact){
		try{
			jess.assertString(jessFact);
			jess.executeCommand("(facts)");
		}
		catch(JessException je){
			je.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean done() {
		return false;
	}
	
}

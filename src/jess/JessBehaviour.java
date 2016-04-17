package jess;

import java.io.FileReader;
import java.io.IOException;

import jade.core.Agent;

public class JessBehaviour extends JessBehaviourBase {
	// the Jess engine
	private jess.Rete jess;
	// maximum number of passes that a run of Jess can execute before giving
	// control to the agent
	private static final int MAX_JESS_PASSES = 3;

	public JessBehaviour(Agent agent, String jessFile){
		super(agent);
		System.out.println("Working directory: " + System.getProperty("user.dir"));
        jess = new Rete();
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
		}
		catch(JessException je){
			je.printStackTrace();
			return false;
		}

        if(!isRunnable()) restart();

        return true;
	}

	
}
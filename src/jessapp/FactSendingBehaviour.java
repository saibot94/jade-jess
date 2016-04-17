package jessapp;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class FactSendingBehaviour extends  JessBehaviourBase{
    private String fact;
    public FactSendingBehaviour(Agent agent, String fact) {
        super(agent);
        this.fact = fact;
    }

    @Override
    public void action() {
        if(fact != null){
            //get the jess engine from the yellow pages
            ArrayList<AID> agents = findAgentsOfType("jess-engine");
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            for(AID agent : agents){
                msg.addReceiver(agent);
            }

            msg.setContent(fact);
            myAgent.send(msg);
            if(agents.size() > 0){
                done = true;
            }
            else{
                block();
            }
        }
        else{
            System.err.println("Fact is null, can't assert it!");
            done = true;
        }
    }
}

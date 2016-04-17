package facts;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jess.JessBehaviourBase;

import java.util.ArrayList;

public class FactSendingBehaviour extends JessBehaviourBase {
    private int initialId;
    public FactSendingBehaviour(Agent agent, int initialId) {
        super(agent);
        this.initialId = initialId;
    }

    @Override
    public void action() {
        String fact = buildFact();
        if(fact != null){
            //get the jess engine from the yellow pages
            ArrayList<AID> agents = findAgentsOfType("jess-engine");
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            for(AID agent : agents){
                msg.addReceiver(agent);
            }

            msg.setContent(fact);
            if(agents.size() > 0){
                myAgent.send(msg);
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

    private String buildFact(){
        String fact = "(product (id " + initialId + ") (price 20) (name \"Potato\") (type veg))";
        initialId++;
        return fact;
    }
}

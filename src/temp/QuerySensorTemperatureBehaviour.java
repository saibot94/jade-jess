package temp;

import jade.core.AID;
import jade.core.Agent;
import java.util.ArrayList;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jess.JessBehaviourBase;

/*

This class queries all the sensors
 */
public class QuerySensorTemperatureBehaviour extends JessBehaviourBase {
    private int step = 0;
    private int allAgents;
    private int replyCnt  = 0;
    public QuerySensorTemperatureBehaviour(Agent agent){
        super(agent);
    }

    @Override
    public void action() {
        switch (step){
            case 0:
                sendQueryToAgents();
                break;
            case 1:
                getRepliesFromAgents();
                break;
        }

    }

    private void sendQueryToAgents() {
        ArrayList<AID> agents = findAgentsOfType("sensor");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        for(AID agent : agents){
            msg.addReceiver(agent);
        }
        allAgents = agents.size();

        myAgent.send(msg);
    }

    private void getRepliesFromAgents(){
        ACLMessage msg = myAgent.receive();

        
        if(msg != null){
            if(msg.getPerformative() == ACLMessage.INFORM){
                double temp = Double.parseDouble(msg.getContent());
                ACLMessage engineQuery = new ACLMessage(ACLMessage.CFP);
                engineQuery.setConversationId("assert");
                engineQuery.setContent(msg.getSender().getName() + ":" +temp );

                replyCnt++;
            }
        }
        else{
            block();
        }
        if(allAgents == replyCnt){
            step = 2;
        }
    }

    @Override
    public boolean done(){
        return (step == 2);
    }


}

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
        step = 1;
    }

    private void getRepliesFromAgents(){
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);


        if(msg != null){
            // take the temperature and send it to jess
            double temp =  Double.parseDouble(msg.getContent());
            ACLMessage engineQuery = new ACLMessage(ACLMessage.CFP);
            engineQuery.setConversationId("assert");
            engineQuery.setContent(msg.getSender().getName() + ":" + temp);
            ArrayList<AID> jessEngines = findAgentsOfType("jess-engine");
            for(AID engine : jessEngines){
                engineQuery.addReceiver(engine);
            }

            myAgent.send(engineQuery);
            replyCnt++;
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

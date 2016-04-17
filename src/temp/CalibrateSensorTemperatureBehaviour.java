package temp;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jess.JessBehaviourBase;
import jess.JessException;

public class CalibrateSensorTemperatureBehaviour extends OneShotBehaviour{

    private final boolean needsHeating;
    private final String sensorId;
    private final boolean needsCooling;

    public CalibrateSensorTemperatureBehaviour(Agent agent, String sensorId, boolean needsCooling, boolean needsHeating) {
        super(agent);
        this.sensorId = sensorId;
        this.needsCooling = needsCooling;
        this.needsHeating = needsHeating;
    }

    @Override
    public void action() {
        AID sensorAID = new AID(sensorId, AID.ISLOCALNAME);
        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
        msg.addReceiver(sensorAID);
        if(needsCooling){
            msg.setContent("cool");
        }
        else if(needsHeating){
            msg.setContent("heat");
        }

        myAgent.send(msg);
    }
}

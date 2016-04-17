package temp;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jess.JessAgentBase;

public class TemperatureSensorAgent extends JessAgentBase {
    private double temperature;
    private TemperatureChangeBehaviour tempChangeBehaviour;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        printStatus("[My current temperature -> " + temperature +"]");
    }

    @Override
    protected void setup() {
        registerAsType("sensor", "jess-app");
        setInitialTemperature();
        // this action is called by the jess engine to get the temperature
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage message = myAgent.receive(mt);
                if(message != null){
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(String.valueOf(temperature));

                    send(reply);
                }
                else{
                    block();
                }
            }
        });

        // this action is called by the jess engine to get the temperature
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

            }
        });
        tempChangeBehaviour = new TemperatureChangeBehaviour(this, 3000);


        addBehaviour(tempChangeBehaviour);


        printStatus("started up!");
    }

    private void setInitialTemperature() {
        try{
            Object[] args = this.getArguments();
            if(args.length > 0) {
                temperature = Double.parseDouble((String) args[0]);
            }
            else{
                temperature = 25.0;
            }
        }
        catch (Exception ex){
            temperature = 25.0;
        }
    }
}

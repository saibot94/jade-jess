package jess;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import temp.CalibrateSensorTemperatureBehaviour;

import java.util.ArrayList;
import java.util.Iterator;

public class JessMessageListener extends CyclicBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JessBehaviour jessBehaviour;


	public JessMessageListener(Agent agent, JessBehaviour jessBehaviour){
		super(agent);
		this.jessBehaviour = jessBehaviour;
	}
	@Override
	public void action() {
		try{
			ACLMessage msg = myAgent.receive();
			if(msg != null){
                if("assert".equals(msg.getConversationId())){
                    if(addAssertMessage(msg.getContent())){
                        runTemperatureQuery();
                    }
                }
                else if("clean".equals(msg.getConversationId())){
                    cleanUpForSensor(msg);
                }
			}
			else{
				block();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

    private void cleanUpForSensor(ACLMessage msg) {
        String sensorId = msg.getSender().getLocalName();
        System.out.println("Cleaning temp for sensor: " + sensorId);

        jessBehaviour.removeFacts("modify-temperature");
    }

    private void runTemperatureQuery() {
        try{
        ArrayList<CalibrateSensorTemperatureBehaviour> calibrations = new ArrayList<>();
        Iterator it = jessBehaviour.runQuery("get-temperatures", new ValueVector());
        if(it != null){
            while(it.hasNext()){
                Token t = (Token) it.next();
                Fact f =  t.fact(1);
                String sensorId = f.getSlotValue("sensorId").atomValue(null);
                boolean needsCooling = Boolean.parseBoolean(f.getSlotValue("needsCooling").symbolValue(null));
                boolean needsHeating = Boolean.parseBoolean(f.getSlotValue("needsHeating").symbolValue(null));

                calibrations.add(new CalibrateSensorTemperatureBehaviour(myAgent, sensorId, needsCooling, needsHeating));
            }
//            if(jessBehaviour.addFact("(cleanup-temperatures)")){
//                System.out.println("Cleaned up temperatures");
//            }
//            else {
//                System.err.println("Could not clean temperatures");
//            }
        }

        calibrations.forEach(myAgent::addBehaviour);

        }catch (JessException je){
            je.printStackTrace();
        }
    }

    private boolean addAssertMessage(String content) {
        String[] contentList = content.split(":");
        String name = contentList[0];
        double temp = Double.valueOf(contentList[1]);

        return jessBehaviour.addFact(buildSensorFact(name, temp));
    }

    private String buildSensorFact(String name, double temp) {
        return "(sensorInfo (sensorId "  + name + ") (temperature " + temp + ") )";
    }

}

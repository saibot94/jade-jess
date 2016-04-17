package jessapp;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class TemperatureChangeBehaviour extends TickerBehaviour {
    private boolean heatingIsRunning = false;
    private TemperatureSensorAgent tempAgent;
    private static final double TEMPERATURE_STEP = 0.5;


    public TemperatureChangeBehaviour(TemperatureSensorAgent agent, long millis){
        super(agent, millis);
        this.tempAgent = agent;
    }

    @Override
    protected void onTick() {
        if(heatingIsRunning){
            tempAgent.setTemperature(tempAgent.getTemperature() + TEMPERATURE_STEP);
        }
        else{
            tempAgent.setTemperature(tempAgent.getTemperature() - TEMPERATURE_STEP);
        }
    }
}

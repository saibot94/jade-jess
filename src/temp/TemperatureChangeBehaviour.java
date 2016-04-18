package temp;

import jade.core.behaviours.TickerBehaviour;

public class TemperatureChangeBehaviour extends TickerBehaviour {
    private boolean heatingIsRunning = false;
    private TemperatureSensorAgent tempAgent;
    private static final double TEMPERATURE_STEP = 0.5;

    public void setHeatingIsRunning(boolean heatingIsRunning) {
        this.heatingIsRunning = heatingIsRunning;
    }

    public boolean getHeatingIsRunning(){
        return this.heatingIsRunning;
    }

    public TemperatureChangeBehaviour(TemperatureSensorAgent agent, long millis){
        super(agent, millis);
        this.tempAgent = agent;
    }

    @Override
    protected void onTick() {
        if(getHeatingIsRunning()){
            tempAgent.setTemperature(tempAgent.getTemperature() + TEMPERATURE_STEP);
        }
        else{
            tempAgent.setTemperature(tempAgent.getTemperature() - TEMPERATURE_STEP);
        }
    }
}

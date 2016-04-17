package jess;

import jade.core.behaviours.TickerBehaviour;
import temp.QuerySensorTemperatureBehaviour;

public class Master extends JessAgentBase {
    @Override
    protected void setup() {

        // query all sensors = TickerBehaviour(15 secs) --> QuerySensorTemperatureBehaviour
        addBehaviour(new TickerBehaviour(this, 9000) {
            @Override
            protected void onTick() {
                addBehaviour(new QuerySensorTemperatureBehaviour(myAgent));
            }
        });


        // inform


    }
}

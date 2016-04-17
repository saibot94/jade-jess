### Jade - Jess Application

This application shows how JADE (Java Agent Development Agent) can be mixed with the Jess rule engine to allow a distributed multi-agent application.

### Installation

_To run the test code, do the following:_

1. Extract the lib folder's contents and add jade.jar and jess.jar to the classpath

2. Add a run configuration: 

	- Main class should be: jade.Boot
	- arguments should be: -gui myagent:hello.HelloAgent

3. Run the application and see hello world displayed by the agent


_To run the book code:_

1. Set arguments something similar to:
	- -gui buyer:book.BookBuyerAgent("Harry Potter");seller1:book.BookSellerAgent;seller2:book.BookSellerAgent

2. Add some books in the gui's

3. Watch as the agent buys the one with the best price

_To run the jess code:_

1. Set the arguments to:
    - -gui jess:jess.JessEngineAgent;sensor1:temp.TemperatureSensorAgent("35.0");sensor2:temp.TemperatureSensorAgent("11.0");master:jess.Master

2. See the fact being asserted in the jess code and being printed on the screen
package org.bp.pizza.state;


import java.util.HashMap;


public class StateService {
	private HashMap<String, StateMachine> processingStates=new HashMap<>();

	public StateService(StateMachineBuilder stateMachineBuilder) {
		this.stateMachineBuilder = stateMachineBuilder;
	}


	private StateMachineBuilder stateMachineBuilder = null;

	public ProcessingState sendEvent(String pizzaId, ProcessingEvent event) {
		StateMachine stateMachine;
		synchronized(this){
			stateMachine = processingStates.get(pizzaId);
			if (stateMachine==null) {
				stateMachine=stateMachineBuilder.build();
				processingStates.put(pizzaId, stateMachine);
			}
		}
		return stateMachine.sendEvent(event);
		
	}

	public void removeState(String bookingId) {
		processingStates.remove(bookingId);
	}

}

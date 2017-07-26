package com.demo.voice.process.service;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.scxml.model.State;

/**
 * Atm Status Finite State Machine Example
 * 
 * @see <a href="http://commons.apache.org/scxml/index.html "> Apache Commons
 *      Scxml Library </a>
 * @author ozkans
 *
 */
public class StatusFSM extends AbstractStateMachine {

	/**
	 * State Machine uses this scmxml config file
	 */
	
	private static final String SCXML_CONFIG_STATUS = "status.xml";

	/*
	 * ------------------------------------------------------------------------
	 */
	/* CONSTRUCTOR(S) */
	/*
	 * ------------------------------------------------------------------------
	 */

	public StatusFSM() {
		super(StatusFSM.class.getClassLoader().getResource(SCXML_CONFIG_STATUS));
	}

	/*
	 * ------------------------------------------------------------------------
	 */
	/* HELPER METHOD(S) */
	/*
	 * ------------------------------------------------------------------------
	 */

//	public void firePreDefinedEvent(AtmStatusEventEnum eventEnum) {
//		System.out.println("EVENT: " + eventEnum);
//		this.fireEvent(eventEnum.getEventName());
//	}

	public void callState(String name) {
		this.invoke(name);
	}

	public String getCurrentStateId() {
		Set<?> states = getEngine().getCurrentStatus().getStates();
		State state = (State) states.iterator().next();
		return state.getId();
	}

	public State getCurrentState() {
		Set<?> states = getEngine().getCurrentStatus().getStates();
		return ((State) states.iterator().next());
	}

	public Collection<?> getCurrentStateEvents() {
		return getEngine().getCurrentStatus().getEvents();
	}

	/*
	 * ------------------------------------------------------------------------
	 */
	// STATES
	//
	// Each method below is the activity corresponding to a state in the
	// SCXML document (see class constructor for pointer to the document).
	/*
	 * ------------------------------------------------------------------------
	 */

	public void idle() {
		System.out.println("STATE: idle");		
	}
	
	
	public void welcome() {
		System.out.println("STATE: welcome");
		System.out.println("Hello!");
	}
	public void askUser() {
		System.out.println("STATE: askUser");
		System.out.println("Please speak your userid");
		System.out.println(this.getCurrentState().toString());
		//this.fireEvent("speakUser");
	}
	
	public void userAccountConfirm() {
		System.out.println("STATE: userAccountConfirm");
		System.out.println("Your account is correct or not?");
	}
	
	public void typeInputText() {
		System.out.println("STATE: typeInputText");
		System.out.println("Please type your account in chatbox");
	}

	public void checkUserStat() {
		System.out.println("STATE: checkUserStat");
		System.out.println("I'm checking your account");
	}
	
	public void checkAccountExisted() {
		System.out.println("STATE: checkAccountLocked");
		System.out.println("Your account existed");
		System.out.println("I'm checking your account");
	}
	
	public void resetPasswordSuccess() {
		System.out.println("STATE: resetPasswordSuccess");
	}
	
	public void sendSMS() {
		System.out.println("STATE: sendSMS");
		System.out.println("I have send you a message, please enter code");
	}
	
	public void sendEmail() {
		System.out.println("STATE: sendEmail");
		System.out.println("I have send you an email, please enter code");
	}
	
	public void phoneCall() {
		System.out.println("STATE: phoneCall");
		System.out.println("I have called you, please enter code");
	}
	
	public void userConfirm() {
		System.out.println("STATE: userConfirm");
		System.out.println("Can you active your account?");
	}
	
	public void inform() {
		System.out.println("STATE: inform");
		System.out.println("Thankyou");
	}
	
}

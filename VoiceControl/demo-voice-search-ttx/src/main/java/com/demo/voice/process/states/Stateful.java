package com.demo.voice.process.states;

import com.demo.voice.process.dto.Literal;
import com.demo.voice.process.service.ADService;

public class Stateful {
	private State state;

	public Stateful() {
		state = State.INITIAL;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State performRequest(Literal result, ADService adService) {
		state = state.doSomething(result, adService);
		return state;
	}
	
}

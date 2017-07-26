package com.demo.voice.process.dto;

public class Response {
	protected String text;
	protected String literal;
	protected String dialogMessage;
	protected String code;
	protected boolean isUnderstand;
	protected String currentState;
    
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLiteral() {
		return literal;
	}
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	public String getDialogMessage() {
		return dialogMessage;
	}
	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isUnderstand() {
		return isUnderstand;
	}
	public void setUnderstand(boolean isUnderstand) {
		this.isUnderstand = isUnderstand;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
}

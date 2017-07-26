package com.demo.voice.process.cache;

import java.util.ArrayList;
import java.util.List;

import com.demo.voice.process.states.Stateful;

public class CacheConversation {
	private int level;
	private int countSpeak;
	private String userId;
	private String code;
	private List<String> messageSuccess;
	private List<String> messageFail;
	
	private Stateful stateful;
	
	public CacheConversation() {
		level = 0;
		countSpeak = 0;
		messageSuccess = new ArrayList<String>();
		messageSuccess.add(0, "Thanks for calling, we will help you reset your account. Please spell your User ID");
		messageSuccess.add(1, "");
		messageSuccess.add(2, "Your account is not locked out. Do you need me to reset your password?");
		messageSuccess.add(3, "Your account will be unlock. To do this, we provide SMS, email, phone call for verification. Please confirm your selected option");
		messageSuccess.add(4, "Your account will be unlock. To do this, we provide SMS, email, phone call for verification. Please confirm your selected option");
		messageSuccess.add(5, "");
		messageSuccess.add(6, "Were you able to successfully log in?");
		messageSuccess.add(7, "Thanks for using our services. Good bye");
		
		messageFail = new ArrayList<String>();
		messageFail.add(0, "Sorry, I don't understand.");
		messageFail.add(1, "Sorry, I can't recognize your User ID. Please spell it again");
		messageFail.add(2, "Sorry about that. Please spell you User ID again");
		messageFail.add(3, "Ok, If you need further assistance, please contact the TTX Service Desk at 312.984.3709.");
		messageFail.add(4, "Ok, If you need further assistance, please contact the TTX Service Desk at 312.984.3709.");
		messageFail.add(5, "Sorry, I don't understand.");
		messageFail.add(6, "Sorry, I can't recognize your code. Please spell it again");
		messageFail.add(7, "Sorry for your time. I will transfer your request to our Help Desk section");
		stateful = new Stateful();
	}
	
	public boolean resetAll() {
		level = 0;
		countSpeak = 0;
		userId = null;
		code = null;
		return true;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void resetLevel() {
		level = 0;
	}
	public void incrementLevel() {
		level++;
	}

	public void decreaseLevel() {
		level--;
	}
	
	public void incrementLevel(int num) {
		level+=num;
	}
	
	public String getMessageSuccess() {
		if (level < messageSuccess.size()) {
			return messageSuccess.get(level);
		}
		return "";
	}

	public String getMessageFail() {
		if (level < messageFail.size()) {
			return messageFail.get(level);
		}
		return "";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCountSpeak() {
		return countSpeak;
	}

	public void setCountSpeak(int countSpeak) {
		this.countSpeak = countSpeak;
	}
	
	public void incrementCountSpeak() {
		countSpeak++;
	}
	
	public void resetCountSpeak() {
		countSpeak =  0;
	}

	public Stateful getStateful() {
		return stateful;
	}

	public void setStateful(Stateful stateful) {
		this.stateful = stateful;
	}
	
}

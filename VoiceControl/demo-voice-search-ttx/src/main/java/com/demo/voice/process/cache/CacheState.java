package com.demo.voice.process.cache;

import com.demo.voice.process.service.StatusFSM;

public class CacheState {
	private String userId;
	private String name;
	private String code;
	private String request;
	private StatusFSM statusFSM;
	private long time;
	public CacheState() {
		statusFSM = new StatusFSM();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public StatusFSM getStatusFSM() {
		return statusFSM;
	}

	public void setStatusFSM(StatusFSM statusFSM) {
		this.statusFSM = statusFSM;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
}

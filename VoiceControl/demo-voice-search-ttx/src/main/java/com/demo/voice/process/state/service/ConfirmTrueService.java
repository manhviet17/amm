package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class ConfirmTrueService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM, NluIntentDTO intent, Response response, CacheManager cacheManager,
			ADService adService, FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		org.apache.commons.scxml.model.State state = null;
		String d;
		String userId = cacheManager.getCacheByUid(uid).getUserId();
		switch (statusFSM.getCurrentStateId()) {
		case "userAccountConfirm":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			String userExisted = adService.checkUserExisted(userId);
			processExistedStatus(userExisted, statusFSM, response, adService, userId);
			break;
		case "accountNormal":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			break;
		case "accountLocked":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			break;
		case "accountNotRegisterService":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		case "loginConfirm":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(1)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		}
	}

	public void processExistedStatus(String userExisted, StatusFSM statusFSM, Response response, ADService adService,
			String userId) {
		String d = null;
		org.apache.commons.scxml.model.State state = null;
		switch (userExisted) {
		case "NOT_FOUND":
			statusFSM.fireEvent("user-notexisted");
			state = statusFSM.getCurrentState();
			if (state.getId().equals("askUser")) {
				d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(1)).getExpr();
			} else if (state.getId().equals("inform")) {
				d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(2)).getExpr();
			}
			response.setDialogMessage(d);
			break;
		case "FOUND":
			statusFSM.fireEvent("user-existed");
			String registeredStatus = adService.checkRegisterdServie(userId);
			processRegisterdStatus(registeredStatus, statusFSM, response, adService, userId);
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			break;
		}

	}

	public void processRegisterdStatus(String registeredStatus, StatusFSM statusFSM, Response response,
			ADService adService, String userId) {
		String d = null;
		org.apache.commons.scxml.model.State state = null;
		state = statusFSM.getCurrentState();
		switch (registeredStatus) {
		case "REGISTERED":
			statusFSM.fireEvent("user-register-service");
			String userStatus = adService.checkUserStatus(userId);
			processUserStatus(userStatus, statusFSM, response);
			break;
		case "NOT_REGISTERED":
			statusFSM.fireEvent("user-not-register-service");
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			break;
		}
	}

	public void processUserStatus(String userStatus, StatusFSM statusFSM, Response response) {
		String d = null;
		org.apache.commons.scxml.model.State state = null;
		switch (userStatus) {
		case "NORMAL":
			statusFSM.fireEvent("user-normal");
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			break;
		case "BLOCKED":
			statusFSM.fireEvent("user-locked");
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			break;
		}
	}

}

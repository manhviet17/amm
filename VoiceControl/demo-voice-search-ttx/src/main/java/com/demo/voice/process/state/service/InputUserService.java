package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.CanonicalDTO;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.cache.CacheState;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class InputUserService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM,NluIntentDTO intent, Response response, CacheManager cacheManager, ADService adService,FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		org.apache.commons.scxml.model.State state = null;
		//if (statusFSM.getCurrentStateId().equals("typeInputText")) {
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			String userId = intent.getUserId();
			CanonicalDTO canonicalDTO = fuzzySearch.fuzzySearch(userId.replaceAll(" dot ", ".").replaceAll(" not ", ".").replaceAll("\\s+", ""));
		    userId = canonicalDTO.getText();
			cacheManager.getCacheByUid(uid).setUserId(userId);
			String d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(userId + " "  + d);
			//String userStatus = adService.checkUserStatus(userId);
			//processUserStatus(userStatus, statusFSM, response);
	}
	
//	public void processUserStatus(String userStatus, StatusFSM statusFSM, Response response) {
//		String d, d1 = null;
//		org.apache.commons.scxml.model.State state = null;
//		switch (userStatus) {
//		case "NOT_REGISTER_SELF_SERVICE":
//			statusFSM.fireEvent("user-not-register-service");
//			state = statusFSM.getCurrentState();
//			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
//			response.setDialogMessage(d);
//			break;
//		case "NOT_FOUND":
//			state = statusFSM.getCurrentState();
//			d1 = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
//			statusFSM.fireEvent("user-notexisted");
//			state = statusFSM.getCurrentState();
//			if (state.getId().equals("typeInputText")) {
//				d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
//				response.setDialogMessage(d);
//			} else {
//				if (!state.getId().equals("inform")) {
//					d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
//					response.setDialogMessage(d1 + " " + d);
//				} else {
//					d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(2)).getExpr();
//					response.setDialogMessage(d);
//					statusFSM.fireEvent("init-state");
//				}
//			}
//
//			break;
//		case "BLOCKED":
//			statusFSM.fireEvent("user-locked");
//			state = statusFSM.getCurrentState();
//			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
//			response.setDialogMessage(d);
//			break;
//		case "NORMAL":
//			statusFSM.fireEvent("user-normal");
//			state = statusFSM.getCurrentState();
//			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
//			response.setDialogMessage(d);
//			break;
//		}
//	}

}

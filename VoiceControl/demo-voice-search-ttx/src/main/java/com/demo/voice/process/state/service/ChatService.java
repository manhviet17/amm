package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class ChatService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM, NluIntentDTO intent, Response response, CacheManager cacheManager,
			ADService adService, FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		String name = cacheManager.getCacheByUid(uid).getName();
		org.apache.commons.scxml.model.State state = statusFSM.getCurrentState();
		String d;
		if (statusFSM.getCurrentStateId().equals("userAccountConfirm")) {
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			if (name != null)
				response.setDialogMessage(
						intent.getIntent() + " " + name + " " + cacheManager.getCacheByUid(uid).getUserId() + " " + d);
			else
				response.setDialogMessage(
						intent.getIntent() + " " + cacheManager.getCacheByUid(uid).getUserId() + " " + d);
		} else {
			if (statusFSM.getCurrentStateId().equals("welcome")) {
				d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(1)).getExpr();		
			}
			else {
				d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			}
			if (name != null)
				response.setDialogMessage(intent.getIntent() + " " + name + ", " + d);
			else
				response.setDialogMessage(intent.getIntent() + " " + d);
		}
	}

}

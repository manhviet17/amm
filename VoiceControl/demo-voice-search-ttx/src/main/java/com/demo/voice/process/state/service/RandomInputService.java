package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class RandomInputService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM, NluIntentDTO intent, Response response, CacheManager cacheManager,
			ADService adService, FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		String d = ((org.apache.commons.scxml.model.Data) statusFSM.getEngine().getStateMachine().getDatamodel()
				.getData().get(1)).getExpr();
		org.apache.commons.scxml.model.State state = statusFSM.getCurrentState();
		String d1 = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
		if (statusFSM.getCurrentStateId().equals("userAccountConfirm")) {
			response.setDialogMessage(d + " " + cacheManager.getCacheByUid(uid).getUserId() + " " + d1);
		} else
			response.setDialogMessage(d + " " + d1);
	}

}

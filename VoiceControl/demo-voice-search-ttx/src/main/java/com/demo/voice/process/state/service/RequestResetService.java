package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class RequestResetService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM, NluIntentDTO intent, Response response, CacheManager cacheManager,
			ADService adService, FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		statusFSM.fireEvent(intent.getIntent());
		if (intent.getName() != null) {
			cacheManager.getCacheByUid(uid).setName(intent.getName());
		}
		cacheManager.getCacheByUid(uid).setRequest(intent.getRequest());
		org.apache.commons.scxml.model.State state = statusFSM.getCurrentState();
		String d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
		response.setDialogMessage(d);
	}
}

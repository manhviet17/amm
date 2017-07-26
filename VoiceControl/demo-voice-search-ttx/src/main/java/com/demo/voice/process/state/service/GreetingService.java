package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class GreetingService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM,NluIntentDTO intent, Response response, CacheManager cacheManager, ADService adService,FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		statusFSM.fireEvent(intent.getIntent());
		org.apache.commons.scxml.model.State state = statusFSM.getCurrentState();
		String name = intent.getName();
		cacheManager.getCacheByUid(uid).setName(name);
		String d1 = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
		String d2 = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(1)).getExpr();
		response.setDialogMessage(d1 + " " + name + "," + d2);
	}

}

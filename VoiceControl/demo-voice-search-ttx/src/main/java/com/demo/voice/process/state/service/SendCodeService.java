package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.cache.CacheState;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class SendCodeService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM,NluIntentDTO intent, Response response, CacheManager cacheManager, ADService adService,FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		org.apache.commons.scxml.model.State state = null;
		String userId = cacheManager.getCacheByUid(uid).getUserId();
		String email = adService.getUser(userId).getEmail();
		String d = null;
		statusFSM.fireEvent(intent.getIntent());
		state = statusFSM.getCurrentState();
		switch (intent.getIntent()) {
		case "send-code-email":
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr().replace("emailaddress", email);
			break;
		case "send-code-sms":
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(1)).getExpr();
			break;
		case "send-code-phonecall":
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(2)).getExpr();
			break;
		 }
		response.setDialogMessage(d);
		response.setCode(adService.generateCode(userId));
	}

}

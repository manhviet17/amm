package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public class ConfirmFalseService implements StateService {

	@Override
	public void doWork(StatusFSM statusFSM, NluIntentDTO intent, Response response, CacheManager cacheManager,
			ADService adService, FuzzySearch fuzzySearch, String uid) {
		// TODO Auto-generated method stub
		org.apache.commons.scxml.model.State state = null;
		String d;
		switch (statusFSM.getCurrentStateId()) {
		case "userAccountConfirm":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		case "accountNormal":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(4)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		case "accountLocked":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(4)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		case "accountNotRegisterService":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(4)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		case "loginConfirm":
			statusFSM.fireEvent(intent.getIntent());
			state = statusFSM.getCurrentState();
			d = ((org.apache.commons.scxml.model.Data) state.getDatamodel().getData().get(0)).getExpr();
			response.setDialogMessage(d);
			statusFSM.fireEvent("init-state");
			break;
		}
	}

}

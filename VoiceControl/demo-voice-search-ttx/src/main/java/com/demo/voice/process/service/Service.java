package com.demo.voice.process.service;

import org.apache.log4j.Logger;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.Cache;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.cache.CacheState;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.state.service.ChatService;
import com.demo.voice.process.state.service.ConfirmFalseService;
import com.demo.voice.process.state.service.ConfirmTrueService;
import com.demo.voice.process.state.service.GreetingService;
import com.demo.voice.process.state.service.InputCodeService;
import com.demo.voice.process.state.service.InputUserService;
import com.demo.voice.process.state.service.RandomInputService;
import com.demo.voice.process.state.service.RequestResetService;
import com.demo.voice.process.state.service.SendCodeService;
import com.demo.voice.process.state.service.StateService;
import com.demo.voice.process.utils.Normalize;
import com.demo.voice.process.utils.XMLProcessor;

public class Service {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Service.class);

	private Cache cache;
	private CacheManager cacheManager;
	private Normalize normalize;
	private XMLProcessor xmlProcessor;
	private ADService adService;
	private FuzzySearch fuzzySearch;

	public Response fsm(NluIntentDTO intent, String uid) {
		Response response = new Response();
		StateService stateService = null;
		CacheState cache = cacheManager.getCacheByUid(uid);
		cache.setTime(System.currentTimeMillis());
		StatusFSM statusFSM = cache.getStatusFSM();
		if (statusFSM.getCurrentStateId().equals("idle")) {
			cacheManager.resetCache(uid);
			cache = cacheManager.getCacheByUid(uid);
			cache.setTime(System.currentTimeMillis());
			statusFSM = cache.getStatusFSM();
		}
		org.apache.commons.scxml.model.State state = statusFSM.getCurrentState();
		if (intent.getIntent() != null) {
			switch (intent.getIntent()) {
			case "greeting":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new GreetingService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "request-reset":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new RequestResetService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "request-reset-userid":
			case "input-userid":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new InputUserService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "confirm-true":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new ConfirmTrueService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "confirm-false":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new ConfirmFalseService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "send-code-email":
			case "send-code-sms":
			case "send-code-phonecall":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new SendCodeService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "input-code":
				if (state.getTransitionsList(intent.getIntent()) != null) {
					stateService = new InputCodeService();
					response.setUnderstand(true);
				} else {
					stateService = new RandomInputService();
				}
				break;
			case "unspecified-na-na":
				stateService = new RandomInputService();
				break;
			default:
				stateService = new ChatService();
				response.setUnderstand(true);
				break;
			}
		} else {
			stateService = new RandomInputService();
		}
		stateService.doWork(statusFSM, intent, response, cacheManager, adService, fuzzySearch, uid);
		response.setCurrentState(statusFSM.getCurrentStateId());
		logger.info(cache.getName());
		return response;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public void setNormalize(Normalize normalize) {
		this.normalize = normalize;
	}

	public void setXmlProcessor(XMLProcessor xmlProcessor) {
		this.xmlProcessor = xmlProcessor;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setAdService(ADService adService) {
		this.adService = adService;
	}

	public void setFuzzySearch(FuzzySearch fuzzySearch) {
		this.fuzzySearch = fuzzySearch;
	}

	public boolean resetConversation(String uid) {
		return cacheManager.resetCache(uid);
	}

}
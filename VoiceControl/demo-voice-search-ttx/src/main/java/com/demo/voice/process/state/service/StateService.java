package com.demo.voice.process.state.service;

import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.process.cache.CacheManager;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.ADService;
import com.demo.voice.process.service.FuzzySearch;
import com.demo.voice.process.service.StatusFSM;

public interface StateService {
	void doWork(StatusFSM statusFSM,NluIntentDTO intent, Response response, CacheManager cacheManager, ADService adService,FuzzySearch fuzzySearch, String uid);
}

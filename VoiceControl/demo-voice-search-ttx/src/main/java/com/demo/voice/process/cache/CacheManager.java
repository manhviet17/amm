package com.demo.voice.process.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

	private static Map<String, CacheState> cacheManager;

	public CacheManager() {
		cacheManager = new ConcurrentHashMap<String, CacheState>();
	}

	public CacheState getCacheByUid(String uid) {

		if (cacheManager.get(uid) != null) {
			return cacheManager.get(uid);
		} else {
			CacheState cache = new CacheState();
			cacheManager.put(uid, cache);
			return cacheManager.get(uid);
		}
	}
	
	public Map<String, CacheState> getAllCache() {
		return cacheManager;
	}
	
	public boolean resetCache(String uid) {
		if(cacheManager.get(uid) !=null) {
			cacheManager.remove(uid);
			CacheState cache = new CacheState();
			cacheManager.put(uid, cache);
			return true;
		}
		return false;
	}
	
	public CacheState removeCache(String uid) {
		return cacheManager.remove(uid);
	}

}

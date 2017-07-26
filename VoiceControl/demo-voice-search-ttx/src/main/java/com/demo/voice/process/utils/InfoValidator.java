package com.demo.voice.process.utils;

import com.demo.voice.process.cache.AccountInfo;
import com.demo.voice.process.cache.Cache;

public class InfoValidator {
	private Cache cache;

	public boolean isValidName(String name) {
		if (cache.getNames().contains(name)) {
			return true;
		}
		return false;
	}

	public boolean isValidPersonId(String personId) {
		if (cache.getPersonIds().contains(personId)) {
			return true;
		}
		return false;
	}

	public boolean isValidAccountId(String accountId) {
		if (cache.getAccountIds().contains(accountId)) {
			return true;
		}
		return false;
	}

	public boolean isValidDate(String date) {
		if (cache.getDates().contains(date)) {
			return true;
		}
		return false;
	}

	public boolean isValidInfo(String name, String personId, String accountId,
			String date) {
		AccountInfo acountInfo = cache.getPersonIdToAccountInfo().get(personId);
		if (acountInfo != null) {
			return name != null && name.equalsIgnoreCase(acountInfo.getName())
					|| accountId != null && accountId.equals(acountInfo.getAccountId())
					|| date != null && date.trim().equals(acountInfo.getDate());
		}
		return false;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}

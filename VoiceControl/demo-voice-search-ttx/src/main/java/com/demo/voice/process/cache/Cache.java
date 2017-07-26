package com.demo.voice.process.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.demo.voice.process.model.OtherData;

public class Cache {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Cache.class);

	private OtherData otherData;

	private Map<String, String> accountIdToValue = new ConcurrentHashMap<String, String>();

	private Map<String, String> normalizeAmountData = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());

	private Map<String, String> normalizeNumberData = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	
	private Map<String, String> unitData = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	
	private Map<String, String> cmdData = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	
	// personId -> AccountInfo
	private Map<String, AccountInfo> personIdToAccountInfo = new ConcurrentHashMap<String, AccountInfo>();
	
	// accountId -> name
	private Map<String, String> accountIdToName = new ConcurrentHashMap<String, String>();

	private Set<String> names = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private Set<String> accountIds = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private Set<String> personIds = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private Set<String> dates = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private Set<String> blockAccounts = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	// config path
	private String accountInfoFolder;
	private String accountFile;
	private String otherFolder;
	private String normalizeNumberFile;
	private String normalizeAmountFile;
	private String unitFile;
	private String cmdFile;

	public void buildCache() {
		logger.info("Starting build cache...");

		Map<String, AccountInfo> accountInfos = CacheLoader
				.getInfoData(accountInfoFolder);
		Map<String, String> accountData = CacheLoader.getMapDataFromFile(accountFile);
		List<OtherData> others = CacheLoader.getOthers(otherFolder);
		
		Map<String, String> normalizeNumber = CacheLoader.getMapDataFromFile(normalizeNumberFile);
		Map<String, String> normalizeAmount = CacheLoader.getMapDataFromFile(normalizeAmountFile);
		Map<String, String> unitData = CacheLoader.getMapDataFromFile(unitFile);
		Map<String, String> cmdData = CacheLoader.getMapDataFromFile(cmdFile);

		buildAccountInfoCache(accountInfos);
		buildOtherCache(others);
		buildNomalizeAmountCache(normalizeAmount);
		buildNomalizeNumberCache(normalizeNumber);
		buildUnitCache(unitData);
		buildCmdCache(cmdData);
		buildAccountDataCache(accountData);

		logger.info("Finish build cache...");
	}

	private void buildAccountInfoCache(Map<String, AccountInfo> map) {
		personIdToAccountInfo.putAll(map);

		Set<String> ids = map.keySet();

		for (String id : ids) {
			AccountInfo accountInfo = map.get(id);
			names.add(accountInfo.getName());
			accountIds.add(accountInfo.getAccountId());
			personIds.add(id);
			dates.add(accountInfo.getDate());
			accountIdToName.put(accountInfo.getAccountId(), accountInfo.getName());
		}
	}

	// Builde Cache
	private void buildNomalizeAmountCache(Map<String, String> map) {
		normalizeAmountData.putAll(map);
	}

	private void buildNomalizeNumberCache(Map<String, String> map) {
		normalizeNumberData.putAll(map);
	}
	
	private void buildUnitCache(Map<String, String> map) {
		unitData.putAll(map);
	}
	
	private void buildCmdCache(Map<String, String> map) {
		cmdData.putAll(map);
	}
	
	private void buildAccountDataCache(Map<String, String> map) {
		accountIdToValue.putAll(map);
	}
	
	private void buildOtherCache(List<OtherData> others) {
		otherData = others.get(0);
	}

	// Get data from Cache
	public OtherData getOtherData() {
		return otherData;
	}

	public Map<String, String> getNormalizeAmountData() {
		return normalizeAmountData;
	}
	
	public Map<String, String> getUnitData() {
		return unitData;
	}
	
	public Map<String, String> getCmdData() {
		return cmdData;
	}

	public String getAccountValue(String accountId) {
		return accountIdToValue.get(accountId);
	}
	
	public String setAccountValue(String accountId, String value) {
		return accountIdToValue.put(accountId, value);
	}
	
	public Set<String> getNames() {
		return names;
	}

	public Set<String> getAccountIds() {
		return accountIds;
	}

	public Set<String> getPersonIds() {
		return personIds;
	}

	public Set<String> getDates() {
		return dates;
	}

	public Map<String, AccountInfo> getPersonIdToAccountInfo() {
		return personIdToAccountInfo;
	}
	
	public Set<String> getBlockAccounts() {
		return blockAccounts;
	}

	public Map<String, String> getAccountIdToName() {
		return accountIdToName;
	}

	public Map<String, String> getNormalizeNumberData() {
		return normalizeNumberData;
	}
	
	// Set value
	public void setOtherFolder(String otherFolder) {
		this.otherFolder = otherFolder;
	}

	public void setAccountFile(String accountFile) {
		this.accountFile = accountFile;
	}

	public void setAccountInfoFolder(String accountInfoFolder) {
		this.accountInfoFolder = accountInfoFolder;
	}

	public void setNormalizeNumberFile(String normalizeNumberFile) {
		this.normalizeNumberFile = normalizeNumberFile;
	}

	public void setNormalizeAmountFile(String normalizeAmountFile) {
		this.normalizeAmountFile = normalizeAmountFile;
	}

	public void setUnitFile(String unitFile) {
		this.unitFile = unitFile;
	}
	
	public void setCmdFile(String cmdFile) {
		this.cmdFile = cmdFile;
	}
}

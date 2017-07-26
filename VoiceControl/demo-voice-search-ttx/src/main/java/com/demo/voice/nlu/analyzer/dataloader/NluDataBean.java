/*
 * NluDataBean.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.dataloader;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jregex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.demo.voice.nlu.analyzer.lookup.service.LocalLookupServiceImpl;
import com.demo.voice.nlu.tagger.processor.Prediction;
import com.directv.nlu.lookup.common.dataloader.NluDataLoaderService;
import com.directv.nlu.lookup.common.dataloader.NluDataType;
import com.directv.nlu.lookup.common.dto.TypeIndex;
import com.directv.nlu.lookup.common.normalize.TextNormalizer;
import com.directv.nlu.lookup.keywordmatching.trie.manager.HolidaysTrie;
import com.directv.nlu.lookup.keywordmatching.trie.manager.TrieManager;
import com.directv.nlu.lookup.service.LookupService;

/**
 *
 * The Class NluDataBean.java.
 *
 * @author Dung Tran
 *
 */
public class NluDataBean {

	protected static final Logger logger = LoggerFactory
			.getLogger(NluDataBean.class);

	//private NormalizeService<MentionType> normalizeService = null;

	private Map<NluDataType, NluDataLoaderService<?>> dataMap = null;

	private String version = "";

	private String completeVersion = "";

	private LookupService lookupService;

	private Map<TypeIndex, Boolean> fuzzyEnableMap = null;

	private Map<String, String[]> numberMap = null;

	public NluDataBean() {
	}

	/**
	 * @return the fuzzyEnableMap
	 */
	public Map<TypeIndex, Boolean> getFuzzyEnableMap() {
		return fuzzyEnableMap;
	}

	/**
	 * @param fuzzyEnableMap
	 *            the fuzzyEnableMap to set
	 */
	public void setFuzzyEnableMap(Map<TypeIndex, Boolean> fuzzyEnableMap) {
		this.fuzzyEnableMap = fuzzyEnableMap;
	}

	/**
	 * @param numberMap
	 *            the numberMap to set
	 */
	public void setNumberMap(Map<String, String> numberMap) {

		for (String key : numberMap.keySet()) {
			String[] values = StringUtils
					.commaDelimitedListToStringArray(numberMap.get(key));
			if (values != null && values.length > 0) {
				if (this.numberMap == null) {
					this.numberMap = new HashMap<String, String[]>();
				}
				this.numberMap.put(key, values);
			}
		}
	}

	public Map<String, String[]> getNumberMap() {
		return this.numberMap;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCompleteVersion() {
		return completeVersion;
	}

	/**
	 * @param normalizeService
	 *            the normalizeService to set
	 */
//	public void setNormalizeService(
//			NormalizeService<MentionType> normalizeService) {
//		this.normalizeService = normalizeService;
//	}
//
//	public NormalizeService<MentionType> getNormalizeService() {
//		return normalizeService;
//	}

	/**
	 * @param dataMap
	 *            the dataMap to set
	 */
	public void setDataMap(Map<NluDataType, NluDataLoaderService<?>> dataMap) {
		this.dataMap = dataMap;
	}

	public void loadAll() {
		List<Map.Entry<NluDataType, List<String>>> fileNamesList = new ArrayList<Map.Entry<NluDataType, List<String>>>();

		loadAll(fileNamesList);

		completeVersion = toCompleteVersionString(fileNamesList);

	}

	private String toCompleteVersionString(
			List<Map.Entry<NluDataType, List<String>>> fileNamesList) {
		StringBuilder sb = new StringBuilder("");

		sb.append(version);

		if (fileNamesList != null && fileNamesList.size() > 0) {

			for (Map.Entry<NluDataType, List<String>> entry : fileNamesList) {
				List<String> fileNames = entry.getValue();

				if (fileNames != null && fileNames.size() > 0) {

					sb.append(";(").append(entry.getKey()).append(")");

					int size = fileNames.size();

					for (int i = 0; i < size; i++) {
						if (i != 0) {
							sb.append(",");
						}

						String fileName = fileNames.get(i);

						if (fileName != null && !"".equals(fileName)) {
							sb.append(fileName.substring(
									fileName.lastIndexOf("/") + 1,
									fileName.lastIndexOf(".")));
						}
					}
				}
			}
		}

		return sb.toString();
	}

	public void load(NluDataType type, NluDataLoaderService<?> service,
			List<String> fileNames) {
		logger.debug("Start loading service: {}", type.name());

		synchronized (NluDataBean.class) {
			if (fileNames != null) {

				service.load(fileNames);

				for (String fileName : fileNames) {
					logger.debug("Loaded: {}", fileName);
				}

			} else {
				try {
					service.load(null);
				} catch (Throwable t) {
					logger.error(type + " failed to load.");
				}
			}
		}

		logger.debug("Stop loading service: {}", type.name());
	}

	public void loadAll(List<Map.Entry<NluDataType, List<String>>> fileNamesList) {

		if (dataMap != null && dataMap.size() > 0) {

			List<Map.Entry<NluDataType, NluDataLoaderService<?>>> list = new ArrayList<Map.Entry<NluDataType, NluDataLoaderService<?>>>();

			for (Map.Entry<NluDataType, NluDataLoaderService<?>> entry : dataMap
					.entrySet()) {
				list.add(entry);
			}

			Collections
					.sort(list,
							new Comparator<Map.Entry<NluDataType, NluDataLoaderService<?>>>() {
								public int compare(
										Map.Entry<NluDataType, NluDataLoaderService<?>> entry1,
										Map.Entry<NluDataType, NluDataLoaderService<?>> entry2) {
									return entry1.getValue().getOrder()
											- entry2.getValue().getOrder();
								}
							});

			for (Map.Entry<NluDataType, NluDataLoaderService<?>> entry : list) {
				NluDataLoaderService<?> service = entry.getValue();

				if (service == null) {
					continue;
				}

				List<String> fileNames = new ArrayList<String>();

				this.load(entry.getKey(), service, fileNames);

				fileNamesList
						.add(new AbstractMap.SimpleEntry<NluDataType, List<String>>(
								entry.getKey(), fileNames));
			}

			if (lookupService == null) {
				lookupService = new LocalLookupServiceImpl(
						(TrieManager) getDataObject(NluDataType.LOOKUP_TRIE),
						(HolidaysTrie) getDataObject(NluDataType.HOLIDAYS_TRIE));
			}

			// replace HashMap of Sentence by Array of Sentence
			// TrieManager trie = getLookupTrie();
			// trie.moveData();
		} else {
			logger.error("Empty Data Map. Loading was unsuccessful.");
		}
	}

	public void load(NluDataType dataType) {
		load(dataType, null);
	}

	public void load(NluDataType dataType, List<String> fileNames) {

		NluDataLoaderService<?> service = dataMap.get(dataType);

		if (service != null) {
			this.load(dataType, service, fileNames);
		}
	}

	public Object getDataObject(NluDataType dataType) {
		NluDataLoaderService<?> service = dataMap.get(dataType);

		if (service != null) {
			return service.getData();
		}

		return null;
	}

	/**
	 * @return the timePattern
	 */
	public Pattern getTimePattern() {
		return (Pattern) getDataObject(NluDataType.TIME_PATTERN);
	}

	/**
	 * @return the textNormalizer
	 */
	public TextNormalizer getTextNormalizer() {
		return (TextNormalizer) getDataObject(NluDataType.TEXT_NORMALIZE);
	}

	/**
	 * @return the numberPattern
	 */
	public Pattern getNumberPattern() {
		return (Pattern) getDataObject(NluDataType.NUMBER_PATTERN);
	}

	/**
	 * @return the prediction
	 */
	public Prediction getPrediction() {
		return (Prediction) getDataObject(NluDataType.PREDICTION);
	}

	public LookupService getLookupService() {

		return lookupService;
	}

	public String getVersion() {
		return version;
	}

	public void setLookupService(LookupService lookupService) {
		this.lookupService = lookupService;
	}
}

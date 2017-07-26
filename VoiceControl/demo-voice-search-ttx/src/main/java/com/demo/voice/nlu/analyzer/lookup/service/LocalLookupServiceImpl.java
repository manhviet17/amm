package com.demo.voice.nlu.analyzer.lookup.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.directv.nlu.lookup.common.dto.Feature;
import com.directv.nlu.lookup.common.dto.HolidayResponse;
import com.directv.nlu.lookup.common.dto.Item;
import com.directv.nlu.lookup.common.dto.TypeIndex;
import com.directv.nlu.lookup.common.indexer.RankNode;
import com.directv.nlu.lookup.keywordmatching.trie.manager.HolidaysTrie;
import com.directv.nlu.lookup.keywordmatching.trie.manager.TrieManager;
import com.directv.nlu.lookup.service.LookupService;

/**
 * The Class LookupServiceImpl.
 *
 * @author Vinh Nguyen
 */
public class LocalLookupServiceImpl implements LookupService {

	private static Logger logger = LoggerFactory.getLogger(LocalLookupServiceImpl.class);

	private TrieManager trie = null;

	public LocalLookupServiceImpl(TrieManager trie, HolidaysTrie holidaysTrie) {
		this.trie = trie;
	}

	@Override
	public List<Feature> getLookFeature(String text) {
		logger.info("getLookFeature");

		return trie.searchExactMatching(text);
	}

	@Override
	public List<int[]> getPosition(int keySearchIndex, Boolean isAkaName) {
		logger.info("getPosition");

		List<int[]> positionList;

		if (isAkaName) {
			positionList = trie.checkAkaTitle(keySearchIndex);
		} else {
			positionList = trie.checkFullName(keySearchIndex);
		}

		return positionList;
	}

	@Override
	public List<String> findOriginalValue(String value, TypeIndex typeIndex) {
		logger.info("findOriginalValue");

		List<String> results = null;

		// get original value if it has typeIndex was sportTeam
//		if (TypeIndex.sportteam.equals(typeIndex)) {
//			results = trie.findPostProcessingSportTeam(value, typeIndex);
//		}
//		// get original value of tone or mood
//		else if (TypeIndex.tone.equals(typeIndex)
//				|| TypeIndex.mood.equals(typeIndex)) {
//			results = trie.getOriginalToneMood(value, typeIndex);
//		} else {

			results = trie.findPostProcessing(value, typeIndex);
//		}

		return results;
	}

	@Override
	public List<Item> findItems(String value, TypeIndex typeIndex) {
		logger.info("findItems");

		return trie.findItem(value, typeIndex);
	}

	@Override
	public List<RankNode> searchFuzzyMatching(String value, TypeIndex typeIndex) {
		logger.info("searchFuzzyMatching");

		return trie.searchFuzzyMatching(value, typeIndex);
	}

	@Override
	public String getSentence(int sentenceIndex) {

		return trie.getSentences()[sentenceIndex].getValue();
	}

	@Override
	public HolidayResponse getHoliday(int yearValue, String keywordSearch) {
		HolidayResponse holidayResponse = new HolidayResponse();

		return holidayResponse;
	}
}

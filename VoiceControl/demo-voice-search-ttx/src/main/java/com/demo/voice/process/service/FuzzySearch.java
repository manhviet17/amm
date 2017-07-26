package com.demo.voice.process.service;

import java.util.ArrayList;
import java.util.List;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.CanonicalDTO;
import com.demo.voice.nlu.analyzer.dto.CanonicalElementDTO;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;
import com.directv.nlu.lookup.common.dto.TypeIndex;
import com.directv.nlu.lookup.common.indexer.RankNode;

public class FuzzySearch {
	private NluDataBean dataBean;
	
	public CanonicalDTO fuzzySearch(String userId) {
		String value;
		// normalizer value
		value = NLUAnalyzerUtils.normalizeString(userId);

		// get fuzzy value
		CanonicalDTO canonicalDTO = getFuzzyValue(value, TypeIndex.accountId);
		return canonicalDTO;
	}

	private CanonicalDTO getFuzzyValue(String value, TypeIndex typeIndex) {
		List<RankNode> resultNodes = dataBean.getLookupService().searchFuzzyMatching(value, typeIndex);
	
		CanonicalDTO canonicalDTO = new CanonicalDTO();
		canonicalDTO.setValue(value);
	
		if (resultNodes != null) {
			int numberOfResults = Math.min(resultNodes.size(), 6);
			
			
			addCanonicalValue(canonicalDTO, resultNodes, numberOfResults, false);
			
			// determine exactMatchFlag here
			if (numberOfResults > 0) {
				canonicalDTO.setExactMatchFlag(determineExactMatchFlag(resultNodes.get(0), canonicalDTO.getText()));
			}
		}
		
		return canonicalDTO;
	}
	
	private void addCanonicalValue(CanonicalDTO canonicalDTO, List<RankNode> resultNodes, int sizeOfResult, boolean isAka) {
		
		List<CanonicalElementDTO> canonicalElements = new ArrayList<CanonicalElementDTO>();
		
		for (int ix = 0; ix < sizeOfResult; ix++) {
			int indexOfValue = resultNodes.get(ix).getItem().getSentenceIndex();
			// get checkFullName from lookup service
			List<int[]> indexes = dataBean.getLookupService().getPosition(indexOfValue, isAka);
			if (indexes != null) {
				// check
				for (int[] idxs : indexes) {
					CanonicalElementDTO canonicalElementDTO = new CanonicalElementDTO();
					// get sentence from lookup service
					String returnedSentence = dataBean.getLookupService().getSentence(idxs[0]);
					if (returnedSentence != null) {
						canonicalElementDTO.setName(returnedSentence);
					}

					// check duplicate
					if (!canonicalElements.contains(canonicalElementDTO)) {
						canonicalElements.add(canonicalElementDTO);
					}
				}
			}

			// add canonical elements to canonicalDTO
			canonicalDTO.setCanonicals(canonicalElements);
		}
	}
	
	/**
	 * if the difference between the query and return result is equals 0 and number of character is greater than more 4 
		so the exactMatchFlag is true
	 * @param rankNode
	 * @param firstResultInCanonical
	 * @return
	 */
	private boolean determineExactMatchFlag(RankNode rankNode, String firstResultInCanonical) {
		int numberOfCharacters = firstResultInCanonical.trim().length();
		if (rankNode.getRankIndex().getDistance() == 0 && numberOfCharacters >= NLUAnalyzerUtils.CHARACTER_COUNT_THRESHOLD) {
			return true;
		}
		
		return false;
	}

	public void setDataBean(NluDataBean dataBean) {
		this.dataBean = dataBean;
	}
	
}

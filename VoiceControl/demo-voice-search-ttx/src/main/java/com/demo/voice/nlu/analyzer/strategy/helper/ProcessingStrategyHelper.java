/*
 * ProcessingStrategyHelper.java
 *
 * Copyright (c) 2013 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.strategy.helper;

import java.util.ArrayList;
import java.util.List;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.CanonicalDTO;
import com.demo.voice.nlu.analyzer.dto.CanonicalElementDTO;
import com.demo.voice.nlu.tagger.dto.LiteralTag;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;
import com.directv.nlu.lookup.common.dto.Feature;
import com.directv.nlu.lookup.common.dto.FeatureEnums;
import com.directv.nlu.lookup.common.dto.TypeIndex;
import com.directv.nlu.lookup.common.indexer.RankNode;

/**
 * The Class ProcessingStrategyHelper.
 *
 * @author tttran
 */
public class ProcessingStrategyHelper {

	private NluDataBean dataBean = null;

	private static final String DATE_FORMAT = "%02d/%02d/%04d";

	public ProcessingStrategyHelper() {
	}

	public ProcessingStrategyHelper(NluDataBean dataBean) {
		this.dataBean = dataBean;
	}

	/**
	 * @param dataBean
	 *            the dataBean to set
	 */
	public void setDataBean(NluDataBean dataBean) {
		this.dataBean = dataBean;
	}

	private boolean isFuzzyEnable(TypeIndex typeIndex) {
		if (dataBean == null || typeIndex == null
				|| dataBean.getFuzzyEnableMap() == null
				|| dataBean.getFuzzyEnableMap().isEmpty()
				|| dataBean.getFuzzyEnableMap().get(typeIndex) == null) {
			return false;
		}

		return dataBean.getFuzzyEnableMap().get(typeIndex).booleanValue();
	}

	/**
	 *
	 * @param target
	 * @param addition
	 * @param delimiter
	 * @return
	 */
	public String addTo(String target, String addition, String delimiter) {
		if (target == null || "".equals(target)) {
			return addition;
		} else if (addition == null || "".equals(addition)) {
			return target;
		} else {
			return (new StringBuffer()).append(target).append(delimiter)
					.append(addition).toString();
		}
	}

	/**
	 *
	 * @param type
	 * @param target
	 * @param intentField
	 * @param unnormalized
	 */
//	public void normalize(MentionType type, String target,
//			StringBuffer intentField, List<String> unnormalized) {
//		String normalized = dataBean.getNormalizeService().normalize(type,
//				target);
//
//		if (normalized != null && normalized.length() > 0) {
//			intentField.append(normalized);
//
//		} else {
//			unnormalized
//					.add(this.getUnnormalizedEntry(type.name(), target, ":"));
//		}
//	}

	/**
	 * To csv.
	 *
	 * @param strings
	 *            the strings
	 * @return the string
	 */
	public String toCsv(List<String> strings) {
		StringBuffer sb = new StringBuffer();
		if (strings == null || strings.size() == 0) {
			return null;
		}

		int i = 0;
		for (String str : strings) {
			if (i++ > 0) {
				sb.append(",");
			}

			sb.append(str);
		}

		return sb.toString();
	}

	/**
	 *
	 * @param type
	 * @param target
	 * @param intentFields
	 * @param unnormalized
	 */
//	public void normalize(MentionType type, String target,
//			List<String> intentFields, List<String> unnormalized) {
//		String normalized = dataBean.getNormalizeService().normalize(type,
//				target);
//
//		if (normalized != null && normalized.length() > 0) {
//			NLUAnalyzerUtils.addNoNullNoDups(intentFields, normalized);
//
//		} else {
//			unnormalized
//					.add(this.getUnnormalizedEntry(type.name(), target, ":"));
//		}
//	}

	public boolean checkDemand(String delivery) {
		if (delivery.contains(NLUAnalyzerUtils.DEMAND)
				|| delivery.contains(NLUAnalyzerUtils.STREAM)
				|| delivery.contains(NLUAnalyzerUtils.STREAMING)) {
			return true;
		}

		return false;
	}

	/**
	 *
	 * @param type
	 * @param value
	 * @param delimiter
	 * @return
	 */
	private String getUnnormalizedEntry(String type, String value,
			String delimiter) {
		return (new StringBuffer()).append(type).append(":").append(value)
				.toString();
	}

	public StringBuffer formatDateTime(StringBuffer savedDate, int date,
			int month, int year) {
		savedDate.append(String.format(DATE_FORMAT, date, month, year));
		return savedDate;
	}

	/**
	 *
	 * @param nluLiteral
	 * @return
	 */
	public String getFullText(NLULiteral nluLiteral) {
		StringBuffer sb = new StringBuffer();

		List<LiteralTag> tags = nluLiteral.getLiteralTags();

		if (tags != null) {
			int i = 0;
			for (LiteralTag tag : tags) {
				if (tag.getValue() == null || "".equals(tag.getValue())) {
					continue;
				}

				if (i++ != 0) {
					sb.append(" ");
				}

				sb.append(tag.getValue().trim());
			}
		}

		return sb.toString();
	}

	public boolean getSportRelated(List<LiteralTag> literalTags) {
		if (literalTags != null && literalTags.size() > 0) {
			for (LiteralTag literalTag : literalTags) {
				if (LabelTags.SportRelated.name().equals(
						literalTag.getTaggedName())) {
					return true;
				}
			}
		}
		return false;
	}

	public String getSecondLiteral(NLULiteral nluLiteral) {
		String secondLiteral = null;
		if (nluLiteral != null) {
			secondLiteral = nluLiteral.toString();
		}

		return secondLiteral;
	}

	/**
	 *
	 * @param value
	 * @param labelFeatures
	 * @param savedResultMaching
	 * @param typeIndex
	 * @return
	 */
	public double countRateMatching(String value, List<Feature> labelFeatures,
			StringBuffer savedResultMaching, TypeIndex typeIndex) {

		double indexMatching = 0;
		double preIndexMatching = 0;
		double penaltyMatching = 0;

		if (value == null || value.length() == 0) {
			return indexMatching;
		}

		if (labelFeatures == null || labelFeatures.size() == 0) {
			return indexMatching;
		}

		List<String> words = NLUAnalyzerUtils.splitPharse(value, " ");
		int size = words.size();
		int sizeOfLabelsFeature = labelFeatures.size();
		int indexOfLabelFeatures = 0;
		int positionOfFeature = 0;

		// check if value of tag has contained feature lookup
		for (int i = 0; i < size; i++) {
			String searchedValue = words.get(i);
			while (indexOfLabelFeatures < sizeOfLabelsFeature) {

				if (labelFeatures.get(indexOfLabelFeatures)
						.getOriginalObservation().equals(searchedValue)) {
					boolean result = false;
//					if (typeIndex.getCode() == TypeIndex.title.getCode()) {
//						for (int idx = 0; idx < TypeIndex.title.getSize(); idx++) {
//							result = labelFeatures.get(indexOfLabelFeatures)
//									.findTypeFeature(
//											FeatureEnums.B_title.getCode()
//													+ idx);
//							if (result) {
//								break;
//							}
//						}
//					} else {
						result = labelFeatures.get(indexOfLabelFeatures)
								.findTypeFeature(typeIndex.getCode());
//					}
					if (result) {
						if (labelFeatures.get(indexOfLabelFeatures)
								.getPositionOfFeature() == positionOfFeature) {
							indexMatching++;

							if (i >= 1) {
								savedResultMaching.append(" ");
							}
							savedResultMaching.append(searchedValue);

							if (indexMatching >= preIndexMatching) {
								preIndexMatching = indexMatching;
								if (positionOfFeature == 1) {
									penaltyMatching++;
								}
							}
							positionOfFeature++;
						} else {
							positionOfFeature = 0;
							if (penaltyMatching > 0) {
								penaltyMatching--;
							}

							if (indexMatching >= preIndexMatching) {
								preIndexMatching = indexMatching;
							}
							if (labelFeatures.get(indexOfLabelFeatures)
									.getPositionOfFeature() == positionOfFeature) {
								indexMatching = 0;
								i = i - 1;
								indexOfLabelFeatures--;
							}
						}
					}

					indexOfLabelFeatures++;
					break;
				}
				indexOfLabelFeatures++;
			}
		}

		if (penaltyMatching >= 1) {
			preIndexMatching += penaltyMatching;
		}
		return (double) (preIndexMatching / size);
	}

	/**
	 * This function to process fuzzy or exact matching value.
	 * When the fuzzyFlag is true then always fuzzy.
	 * if the fuzzyFlag is false then based on countRating to determine fuzzy or exact searching.
	 * @param values
	 * @param labelFeatures
	 * @param typeIndex
	 * @return List<CanonicalDTO>
	 */
	public List<CanonicalDTO> processCanonical(List<String> values,
			List<Feature> labelFeatures, TypeIndex typeIndex) {

		List<CanonicalDTO> canonicals = new ArrayList<CanonicalDTO>();

		boolean fuzzyFlag = isFuzzyEnable(typeIndex);

		if (labelFeatures == null || labelFeatures.size() == 0) {
			return canonicals;
		}

		// always fuzzy if fuzzyFlag is true
		if (fuzzyFlag) {
			for (int index = 0; index < values.size(); index++) {
				String value = values.get(index).trim();
				// normalizer value
				value = NLUAnalyzerUtils.normalizeString(value);

				// get fuzzy value
				CanonicalDTO canonicalDTO = getFuzzyValue(value, typeIndex);
				canonicals.add(canonicalDTO);
			}
		} else {
			for (int index = 0; index < values.size(); index++) {
				CanonicalDTO canonicalDTO;
				String value = values.get(index).trim();
				StringBuffer tmp = new StringBuffer();
				// calculate rate matching
				double rate = countRateMatching(value, labelFeatures, tmp,typeIndex);

				// normalizer value
				value = NLUAnalyzerUtils.normalizeString(value);
				if (rate > 0.5) {
					// get exact matching value
					canonicalDTO = getExactMatchingValue(value, typeIndex);
					canonicals.add(canonicalDTO);
				} else {
					// get fuzzy value
					canonicalDTO = getFuzzyValue(value, typeIndex);
					canonicals.add(canonicalDTO);
				}
			}
		}
		// normalizer
//		if (typeIndex.getCode() == TypeIndex.credit.getCode()) {
//			for (CanonicalDTO canonicalDTO : canonicals) {
//				for (int i = canonicalDTO.getCanonicals().size() - 1; i >= 0; i--) {
//					String name = canonicalDTO.getCanonicals().get(i).getName();
//					// get normalizer
//					String normalizerd = NLUAnalyzerUtils.actorNormalize(name);
//
//					if (!name.equals(normalizerd)) {
//						CanonicalElementDTO tmp = new CanonicalElementDTO();
//						tmp.setName(normalizerd);
//						canonicalDTO.getCanonicals().add(i, tmp);
//					}
//				}
//			}
//		}

		return canonicals;
	}

	/**
	 * The purpose of this function is to get exact matching value.
	 *
	 * @param value
	 *     The value is content of tagged itself.
	 * @param typeIndex
	 *     The typeIndex
	 * @return {@link CanonicalDTO}
	 */
	public CanonicalDTO getExactMatchingValue(String value, TypeIndex typeIndex) {
		CanonicalDTO canonicalDTO = new CanonicalDTO();
		canonicalDTO.setValue(value);
		canonicalDTO.setExactMatchFlag(true);

		CanonicalElementDTO canonicalElementDTO = null;
		List<CanonicalElementDTO> canonicalElements;

		List<String> originalResults = dataBean.getLookupService()
				.findOriginalValue(value, typeIndex);

		if (originalResults == null) {
			return canonicalDTO;
		}
		canonicalElements = new ArrayList<CanonicalElementDTO>();
		for (String str : originalResults) {
			canonicalElementDTO = new CanonicalElementDTO();
			canonicalElementDTO.setName(str);
			if (!canonicalElements.contains(canonicalElementDTO)) {
				canonicalElements.add(canonicalElementDTO);
			}
		}

		canonicalDTO.setCanonicals(canonicalElements);

		return canonicalDTO;
	}

	/**
	 * This function to check typeIndex is sportTeam or credit or title
	 * @param value
	 * @param typeIndex
	 * @return
	 */
	private CanonicalDTO getFuzzyValue(String value, TypeIndex typeIndex) {
		List<RankNode> resultNodes = dataBean.getLookupService().searchFuzzyMatching(value, typeIndex);

		CanonicalDTO canonicalDTO = new CanonicalDTO();
		canonicalDTO.setValue(value);

		if (resultNodes != null) {
			int numberOfResults = Math.min(resultNodes.size(), 6);
			
			boolean isAkaTitle = false;
//			if (TypeIndex.title.equals(typeIndex)) {
//				isAkaTitle = true;
//			}
			
			addCanonicalValue(canonicalDTO, resultNodes, numberOfResults, isAkaTitle);
			
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


	/***
	 * Purpose of this function to get original tone or mood value
	 *
	 * @param toneMoodValueList
	 * @param typeIndex
	 * @return
	 */
	public String getOriginalToneMoodValue(List<String> toneMoodValueList,
			TypeIndex typeIndex) {
		if (toneMoodValueList == null) {
			return null;
		}
		int toneMoodSize = toneMoodValueList.size();

		if (toneMoodSize == 0) {
			return null;
		}

		StringBuffer savedValue = new StringBuffer();
		for (int i = 0; i < toneMoodSize; i++) {
			if (i >= 1) {
				savedValue.append(",");
			}

			List<String> originalResultList = dataBean.getLookupService()
					.findOriginalValue(toneMoodValueList.get(i), typeIndex);
			if (originalResultList != null) {
				int originalResultSize = originalResultList.size();

				for (int j = 0; j < originalResultSize; j++) {
					if (j >= 1) {
						savedValue.append(",");
					}

					savedValue.append(originalResultList.get(j));
				}
			} else {
				savedValue.append(toneMoodValueList.get(i));
			}
		}

		if (savedValue.length() > 0) {
			return savedValue.toString();
		}

		return null;
	}
}
/*
 * TaggerUtils.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.tagger.util;

import java.util.ArrayList;
import java.util.List;

import com.directv.nlu.lookup.common.dto.Feature;
/**
 *
 * The Class TaggerUtils.java
 *
 * @author Dung Tran
 *
 */
public class TaggerUtils {


	/**
	 *
	 * This function to combine feature
	 *
	 * @param dataLookups
	 * @param posTaggers
	 * @param cardinalNumeralFeature
	 * @param timeFeatures
	 *
	 * @return List<Feature>
	 * */
	public static List<Feature> combineFeature(List<Feature> dataLookups, List<Feature> posTaggers,
			List<Feature> cardinalNumeralFeature, List<Feature> timeFeatures) {

		if (posTaggers == null || posTaggers.size() == 0) {
			return new ArrayList<Feature>();
		}

		if (dataLookups == null || dataLookups.size() == 0) {
			return new ArrayList<Feature>();
		}

		int sizeOfPosTagger = posTaggers.size() - 1;
		int sizeOfLookupFeatures = dataLookups.size();

		if (sizeOfLookupFeatures >= sizeOfPosTagger) {
			for (int i = 0; i < sizeOfLookupFeatures; i++) {
				dataLookups.get(i).setPosTagger(posTaggers.get(i).getPosTagger());
			}

			// add cardinal numeral feature
			addCardinalNumeralFeature(cardinalNumeralFeature, dataLookups);

			return addTimeFeture(timeFeatures, dataLookups);
		}
		// check if sizeOfLookupFeatures isn't equal sizeOfPosTagger
		else {
			int savedIndexNotEqual = 0;
			for (int browsingIndex = 0; browsingIndex < sizeOfLookupFeatures; browsingIndex++) {
				// get value of lookup feature
				String originalObservation = dataLookups.get(browsingIndex).getOriginalObservation().trim();
				// set lookup features to posTaggers at the savedIndexNotEqual
				// position
				dataLookups.get(browsingIndex).setPosTagger(posTaggers.get(savedIndexNotEqual).getPosTagger());

				// check if originalObservation isn't equals value of the
				// wordPosTagger at the saveIndexNotEqual position
				if (!originalObservation.equalsIgnoreCase(posTaggers.get(savedIndexNotEqual).getOriginalObservation()
						.trim())) {
					// get originalObservation at the next position
					String orgObserNext = posTaggers.get(savedIndexNotEqual + 1).getOriginalObservation();
					String combinedValue = posTaggers.get(savedIndexNotEqual).getOriginalObservation() + orgObserNext;

					// check if originalObservation starts with of the
					// combinedValue
					if (originalObservation.startsWith(combinedValue)) {
						// check if originalObservation is equal combinedValue
						if (originalObservation.equalsIgnoreCase(combinedValue)) {
							savedIndexNotEqual++;
						}
						// check if originalObservation isn't equal
						// combinedValue
						else {
							// save temporary index
							int temporaryIndex = 1;
							do {
								if ((savedIndexNotEqual + temporaryIndex) == sizeOfPosTagger) {
									break;
								}
								// increasing temporary index
								temporaryIndex++;
								combinedValue += posTaggers.get(savedIndexNotEqual + temporaryIndex)
										.getOriginalObservation();

								// check if combinedValue is equals
								// originalObservation
								if (combinedValue.equalsIgnoreCase(originalObservation)) {
									savedIndexNotEqual += temporaryIndex;
									savedIndexNotEqual--;
									break;
								}
							} while (savedIndexNotEqual < sizeOfPosTagger);
						}
					}// check if isn't startsWith then processing normal
					else {
						if (orgObserNext.contains("'")) {
							savedIndexNotEqual++;
						}
					}
				}

				savedIndexNotEqual++;
			}
			// add cardinal numeral feature
			addCardinalNumeralFeature(cardinalNumeralFeature, dataLookups);
			// dataLookups.add(posTaggers.get(sizeOfPosTagger));
			return addTimeFeture(timeFeatures, dataLookups);
		}
	}

	/**
	 * Additional cardinalNumeralFeature to the lookupFeatures
	 *
	 * @param cardinalNumeralFeature
	 * @param lookupFeatures
	 * @return List<Feature>
	 */
	private static List<Feature> addCardinalNumeralFeature(List<Feature> cardinalNumeralFeature, List<Feature> lookupFeatures) {
		if (cardinalNumeralFeature == null || cardinalNumeralFeature.size() == 0) {
			return lookupFeatures;
		}

		int cardinalIndex = 0;
		int lookupIndex = 0;
		int cardinalSize = cardinalNumeralFeature.size();
		int lookupSize = lookupFeatures.size();

//		while (cardinalIndex < cardinalSize && lookupIndex < lookupSize) {
//			String cardinalValue = cardinalNumeralFeature.get(cardinalIndex).getOriginalObservation();
//			String observationValue = lookupFeatures.get(lookupIndex).getOriginalObservation();
//			if (cardinalValue.equals(observationValue)) {
//				lookupFeatures.get(lookupIndex).addType(FeatureEnums.ordinal.getCode());
//				cardinalIndex++;
//				lookupIndex++;
//			} else {
//				lookupIndex++;
//			}
//
//			// check conditional here, exclude special case.
//			if (cardinalIndex < cardinalSize && lookupIndex == lookupSize) {
//				cardinalIndex++;
//				lookupIndex = 0;
//			}
//
//		}

		return lookupFeatures;
	}

	/***
	 * Additional time feature to lookupFeatures
	 * @param timeFeatures
	 * @param lookupFeatures
	 * @return List<Feature>
	 */
	private static List<Feature> addTimeFeture(List<Feature> timeFeatures, List<Feature> lookupFeatures) {

		if (timeFeatures == null || timeFeatures.size() == 0) {
			return lookupFeatures;
		}

		int timeIndex = 0;
		int lookupIndex = 0;
		int timeSize = timeFeatures.size();
		int lookupSize = lookupFeatures.size();

		while (timeIndex < timeSize && lookupIndex < lookupSize) {
			String timeValue = timeFeatures.get(timeIndex).getOriginalObservation();
			String observationValue = lookupFeatures.get(lookupIndex).getOriginalObservation();
			if (timeValue.equals(observationValue)) {
				// get time feature
//				if (timeFeatures.get(timeIndex).findTypeFeature(TypeIndex.time.getCode())) {
//					lookupFeatures.get(lookupIndex).addType(FeatureEnums.time.getCode());
//				}
				// // get date feature
//				if (timeFeatures.get(timeIndex).findTypeFeature(TypeIndex.date.getCode())) {
//					lookupFeatures.get(lookupIndex).addType(FeatureEnums.date.getCode());
//				}
				// // get year feature
//				if (timeFeatures.get(timeIndex).findTypeFeature(TypeIndex.year.getCode())) {
//					lookupFeatures.get(lookupIndex).addType(FeatureEnums.year.getCode());
//				}

				timeIndex++;
				lookupIndex++;
			} else {
				lookupIndex++;
			}

			if (timeIndex < timeSize && lookupIndex == lookupSize) {
				timeIndex++;
				lookupIndex = 0;
			}

		}

		return lookupFeatures;
	}

}

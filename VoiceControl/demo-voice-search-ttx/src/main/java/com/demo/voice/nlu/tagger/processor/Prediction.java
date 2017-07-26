/*
 * Prediction.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.tagger.processor;

import java.util.ArrayList;
import java.util.List;

import com.demo.voice.nlu.tagger.dto.LiteralTag;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;
import com.directv.nlu.crf.model.Model;
import com.directv.nlu.crf.prediction.CRFPredict;
import com.directv.nlu.lookup.common.dto.Feature;

/**
 *
 * The Class Prediction.java
 *
 * @author Dung Tran
 *
 */

public class Prediction {

   // protected static final Logger logger = LoggerFactory.getLogger(Prediction.class);

	private String taggerCommand = null;

	private Model model;
	private CRFPredict crfPredict;

	/**
	 * @param taggerCommand
	 *            the taggerCommand to set
	 */
	public void setTaggerCommand(String taggerCommand) {
		this.taggerCommand = taggerCommand;
	}

	/**
	 * @param fullText
	 * @return List<Feature> {@link Feature}
	 *
	 * */
//	public List<Feature> combineFeatureOnline(String fullText, List<Feature> dataLookups, Pattern timePattern, Pattern numberPattern,
//			MaxentTagger maxentTagger) {
//
//		return dataLookups;
//	}

	/**
	 * @param features
	 *            features is not contains label
	 * @return List<Features> {@link Feature}
	 *
	 * */
	public List<NLULiteral> execute(List<Feature> features) {
		
		for (Feature feature: features) {
			System.out.println(feature.getOriginalObservation() + " " + feature.toString());
		}
		return executeVersionJava(features, taggerCommand);
	}

	private List<NLULiteral> executeVersionJava(List<Feature> features, String command) {
		List<NLULiteral> nluLiterals = new ArrayList<NLULiteral>();

		// check if features is null or empty then return
		if (features == null || features.size() == 0) {
			return nluLiterals;
		}

		List<String> featureCRF = new ArrayList<String>();
		for (int i = 0; i < features.size(); i++) {
			featureCRF.add(features.get(i).getOriginalObservation() + "\t"
					+ features.get(i).toString());
		}
		// add the end of sentences
		featureCRF.add(NLUAnalyzerUtils.END_OFF_SENTENCES_PREDICTION);
		// add feature here
		CRFPredict crfPredict = new CRFPredict();
		crfPredict.setModel(model);
		crfPredict.addFeature(featureCRF);


		List<String> labels = crfPredict.parser();
		if (labels == null) {
			return null;
		}

		if (labels.size() != (features.size() +1)) {
			return null;
		}

		for (int i = 0; i < labels.size() - 1; i++) {
			features.get(i).setLabel(labels.get(i));
		}

		NLULiteral nluLiteral = new NLULiteral();
		// labelFeatures is added to NLULiteral
		nluLiteral.setLabelFeatures(features);
//		// LiteralTag is added to NLULiteral
		nluLiteral.setLiteralTags(convertLiteral(features));
		nluLiterals.add(nluLiteral);
		// taggedCRF.delete();
		//taggedCRF.clear();


		return nluLiterals;
	}
//	public List<NLULiteral> execute(List<Feature> features, String command) {
//		return null;
//	}

	/**
	 * @param featureList
	 * @return List<LiteralTag> {@link LiteralTag}
	 * */
	private List<LiteralTag> convertLiteral(List<Feature> featureList) {
		List<LiteralTag> literalTags = new ArrayList<LiteralTag>();

		if (featureList == null || featureList.size() == 0) {
			return literalTags;
		}

		StringBuffer savedValue = null;
		String previousLabel = null;
		String currentLabel = null;
		String currentObservation = null;
		LiteralTag literalTag = null;
		int sizeOfFeatures = featureList.size();
		// added by DungTP 2013/08/07
		int indexOfWord = 0;
		
		// added by DungTP 2014/10/21
		boolean onlyHasTitleAndCommand = true;
		boolean hasTitle = false;
		
		for (int i = 0; i < sizeOfFeatures; i++) {
			Feature feature = featureList.get(i);
			currentObservation = feature.getOriginalObservation();
			currentLabel = feature.getLabel();

			// processing label if Genre Tag is returned
//			if (LabelTags.Genre.name().equals(currentLabel)) {
//				if (feature.findTypeFeature(FeatureEnums.musicgenre.getCode())) {
//					currentLabel = LabelTags.MusicGenre.name();
//				} else if (feature.findTypeFeature(FeatureEnums.sportgenre.getCode())) {
//					currentLabel = LabelTags.SportGenre.name();
//				}
//				// has other tags
//				onlyHasTitleAndCommand = false;
//			}
//			
			// check other tags
			if (onlyHasTitleAndCommand) {
				// if the tag is not title tag
				if (!LabelTags.Title.name().equals(currentLabel)) {
					// if the tag is not command tag
					if (!LabelTags.Command.name().equals(currentLabel)) {
						// has other tags
						onlyHasTitleAndCommand = false;
					}
				} else {
					hasTitle = true;
				}
					
			}
			
			// check if it is the first position
			if (i == 0) {
				literalTag = new LiteralTag();
				savedValue = new StringBuffer();

			} else {
				// check if the current label isn't equals the previous label or
				// the current label is stared B_
				// then create a new label
				if (!previousLabel.equals(currentLabel)) {
					
					// normalize command tag if the tag is command
					normalizeTag(literalTag, previousLabel, savedValue);
					
					literalTag.setValue(savedValue.toString());
					literalTags.add(literalTag);

					// reset literalTag
					indexOfWord = 0;
					literalTag = new LiteralTag();
					savedValue = new StringBuffer();
				}
			}

			// set the first label
			literalTag.setTaggedName(currentLabel);
			// set the first value
			if (indexOfWord >= 1) {
				savedValue.append(" ");
			}
			savedValue.append(currentObservation);

			indexOfWord++;
			if (i == (sizeOfFeatures - 1)) {
				literalTag.setValue(savedValue.toString());
				// check if say "show" or "play" only
				// make sure that has only "show" or "play"
				if (i == 0 && (NLUAnalyzerUtils.Show.equals(literalTag.getValue()) || NLUAnalyzerUtils.Play.equals(literalTag.getValue()))) {
					literalTag.setTaggedName(LabelTags.CommandPlay.name());
				} else {
					// normalize command tag if the tag is command
					normalizeTag(literalTag, currentLabel, savedValue);
				}
				
				literalTags.add(literalTag);
			}

			previousLabel = currentLabel;
		}

		// process with "show me", "show", "play me", "play", if only has title and command.
		if (onlyHasTitleAndCommand && hasTitle) {
			processSpecialTag(literalTags);
		}
		
		return literalTags;
	}
	
	/**
	 * This function to get special tag
	 * @param literalTags
	 */
	private void processSpecialTag(List<LiteralTag> literalTags) {
		if (literalTags == null) {
			return;
		}
		
		int size = literalTags.size();
		for (int i = 0; i < size; i++) {
			LiteralTag currentLiteralTag = literalTags.get(i);
			if (NLUAnalyzerUtils.COMMAND_PLAY_MAP.containsKey(currentLiteralTag.getValue())) {
				currentLiteralTag.setTaggedName(LabelTags.CommandPlay.name());
				break;
			}
		}
	}
	
	/**
	 * This function to normalize command tag.
	 * @param commandTag
	 * @param valueOfTag
	 * @return String
	 * 
	 */
	private void normalizeTag(LiteralTag literalTag, String commandTag, StringBuffer valueOfTag) {
		
		if (LabelTags.Command.name().equals(commandTag)) {
			String savedCommandValue = valueOfTag + "";
			if (NLUAnalyzerUtils.COMMAND_SEARCH_MAP.containsKey(savedCommandValue)) {
				// command search
				commandTag = LabelTags.CommandSearch.name();
			} else if (NLUAnalyzerUtils.COMMAND_PLAY_MAP.containsKey(savedCommandValue)) {
				// command play
				commandTag = LabelTags.CommandPlay.name();
			} else if (NLUAnalyzerUtils.COMMAND_BUY_MAP.containsKey(savedCommandValue)) {
				// command buy
				commandTag = LabelTags.CommandBuy.name();
			} else if (NLUAnalyzerUtils.COMMAND_RECORD_MAP.containsKey(savedCommandValue)) {
				// command record
				commandTag = LabelTags.CommandRecord.name();
			}

			literalTag.setTaggedName(commandTag);
		}
	}

	public CRFPredict getCrfPredict() {
		return crfPredict;
	}

	public void setCrfPredict(CRFPredict crfPredict) {
		this.crfPredict = crfPredict;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}

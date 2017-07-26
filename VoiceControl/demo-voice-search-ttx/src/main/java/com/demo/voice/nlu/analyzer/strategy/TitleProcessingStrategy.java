/*
 * TitleProcessingStrategy.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.CanonicalDTO;
import com.demo.voice.nlu.analyzer.dto.CanonicalElementDTO;
import com.demo.voice.nlu.analyzer.dto.MentionType;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;

/**
 *
 * The Class TitleProcessingStrategy.java.
 *
 * @author Dung Tran
 * @author Vinh Nguyen
 *
 */
public class TitleProcessingStrategy extends AbstractProcessingStrategy
		implements ProcessingStrategy {

	private static final LabelTags[] tags = { LabelTags.Title };

	private static final MentionType mentionType = MentionType.TITLE;
	
	private static final boolean post = true;

    private static final boolean alwaysRequired = false;

	protected List<String> titles = null;

	public TitleProcessingStrategy() {
		super();
	}

	public TitleProcessingStrategy(NluDataBean dataBean,
			ProcessingStrategyHelper helper) {
		super(dataBean, helper);
	}

	@Override
	public LabelTags[] getTags() {
		return tags;
	}

	@Override
	public MentionType getMentionType() {
		return mentionType;
	}

	@Override
	public ProcessingStrategy getInstance(NluDataBean dataBean,
			ProcessingStrategyHelper helper) {
		return new TitleProcessingStrategy(dataBean, helper);
	}

	@Override
	public void setValue(LabelTags tag, String value) {
		if (LabelTags.Title.equals(tag)) {
			if (titles == null) {
				titles = new ArrayList<String>();
			}

			NLUAnalyzerUtils.addNoNullNoDups(titles, value);
		}
	}

	@Override
	public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral,
			String timeZone) {
		// TrieManager lookupTrie = dataBean.getLookupTrie();
		if (titles != null && titles.size() > 0) {
			
			//Remove three d in title
			//NLUAnalyzerUtils.removeThreeDInTitle(titles, intent.isThreeD());
			
//			List<CanonicalDTO> titleCanonical = this.helper.processCanonical(
//					titles, nluLiteral.getLabelFeatures(), TypeIndex.title);
//			addTitleCanonical(titleCanonical);
//			intent.setTitles(titleCanonical);
		}
		return intent;
	}

	private void addTitleCanonical(List<CanonicalDTO> titleCanonical) {

		List<String[]> values = new ArrayList<String[]>();
		if (titleCanonical != null && titleCanonical.size() > 0) {
			for (CanonicalDTO canonicalDTO : titleCanonical) {
				for (CanonicalElementDTO canonicalElementDTO : canonicalDTO
						.getCanonicals()) {
					String[] tmpValues = convertLiteral(canonicalElementDTO
							.getName());
					if (tmpValues != null) {
						values.add(tmpValues);
					}
				}
			}

			for (CanonicalDTO canonicalDTO : titleCanonical) {
				List<CanonicalElementDTO> canonicals = canonicalDTO
						.getCanonicals();
				if (values != null && values.size() > 0) {
					for (String[] strs : values) {
						for (String str : strs) {
							CanonicalElementDTO canonicalElementDTO = new CanonicalElementDTO();
							canonicalElementDTO.setName(str);

							if (!canonicals.contains(canonicalElementDTO)) {
								canonicals.add(canonicalElementDTO);
							}
						}
					}
				}
			}
		}
	}

	private Map<String, String[]> getTitleNumberMap() {
		if (dataBean == null || dataBean.getNumberMap() == null) {
			return Collections.emptyMap();
		}

		return dataBean.getNumberMap();
	}

	private String[] convertLiteral(String value) {

		if (value == null || "".equals(value)) {
			return null;
		}

		String[] tokens = value.split(" ");

		int index = -1;

		String[] numbers = null;

		Map<String, String[]> numberMap = getTitleNumberMap();

		for (int i = tokens.length - 1; i >= 0; i--) {
			numbers = numberMap.get(tokens[i].toLowerCase());
			if (numbers != null && numbers.length > 0) {
				index = i;
				break;
			}
		}

		if (index >= 0) {
			String[] values = new String[numbers.length];

			for (int i = 0; i < values.length; i++) {

				StringBuilder sb = new StringBuilder().append("");

				for (int j = 0; j < tokens.length; j++) {
					if (j > 0) {
						sb.append(" ");
					}

					if (index == j) {
						sb.append(numbers[i]);
					} else {
						sb.append(tokens[j]);
					}
				}

				values[i] = sb.toString();
			}

			return values;
		}

		return null;
	}
	
	@Override
	public boolean isPostStrategy() {
		return post;
	}

	@Override
	public boolean isAlwaysRequired() {
		return alwaysRequired;
	}

}
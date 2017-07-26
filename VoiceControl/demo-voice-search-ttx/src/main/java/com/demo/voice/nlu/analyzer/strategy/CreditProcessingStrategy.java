/*
 * CreditProcessingStrategy.java
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
import java.util.List;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.MentionType;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;

/**
 * The Class CreditProcessingStrategy.java.
 *
 * @author Dung Tran
 *
 */
public class CreditProcessingStrategy extends AbstractProcessingStrategy
		implements ProcessingStrategy {

	private static final LabelTags[] tags = { LabelTags.Credit };

	private static final MentionType mentionType = MentionType.CREDIT;

	protected List<String> credits = null;

	public CreditProcessingStrategy() {
		super();
	}

	public CreditProcessingStrategy(NluDataBean dataBean,
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
		return new CreditProcessingStrategy(dataBean, helper);
	}

	@Override
	public void setValue(LabelTags tag, String value) {
		if (LabelTags.Credit.equals(tag)) {
			if (credits == null) {
				credits = new ArrayList<String>();
	         }

	         NLUAnalyzerUtils.addNoNullNoDups(credits, value);
		}
	}

	@Override
	public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral,
			String timeZone) {
		//TrieManager lookupTrie = dataBean.getLookupTrie();
		if (credits != null && credits.size() > 0) {
//			intent.setCredits(this.helper.processCanonical(credits, nluLiteral.getLabelFeatures(),
//					TypeIndex.credit));
		}

		return intent;
	}

}
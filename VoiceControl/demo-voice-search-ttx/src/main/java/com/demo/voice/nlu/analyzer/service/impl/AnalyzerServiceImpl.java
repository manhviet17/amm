/*
 * AnalyzerServiceImpl.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.service.AnalyzerService;
import com.demo.voice.nlu.analyzer.strategy.NluProcessingStrategyContext;
import com.demo.voice.nlu.analyzer.strategy.ProcessingStrategy;
import com.demo.voice.nlu.tagger.dto.LiteralTag;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.processor.Prediction;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;
import com.directv.nlu.lookup.common.dto.Feature;

/**
 * The Class AnalyzerServiceImpl.
 *
 * @author tttran
 */
public class AnalyzerServiceImpl implements AnalyzerService {

	/** The context. */
	protected NluProcessingStrategyContext context = null;

	/**
	 * Sets the context.
	 *
	 * @param context
	 *            the context to set
	 */
	public void setContext(NluProcessingStrategyContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.directv.nlu.discovery.analyzer.service.AnalyzerService#analyze
	 * (java.util.List,
	 * com.directv.nlu.discovery.analyzer.dataloader.NluDataBean,
	 * java.lang.String)
	 */
	public NluIntentDTO analyze(List<NLULiteral> nluLiterals, NluDataBean dataBean, String timeZone) {
		NluIntentDTO nluIntentDTO = new NluIntentDTO();

		if (nluLiterals == null || nluLiterals.size() == 0) {
			return nluIntentDTO;
		}

		NLULiteral nluLiteral = nluLiterals.get(0);
		nluIntentDTO = analyze(nluLiteral, dataBean, timeZone);

		return nluIntentDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.directv.nlu.discovery.analyzer.service.AnalyzerService#analyze(java.
	 * lang.String, com.directv.nlu.discovery.analyzer.dataloader.NluDataBean,
	 * java.lang.String)
	 */
	public NluIntentDTO analyze(String text, NluDataBean dataBean, String timeZone) {

		NluIntentDTO nluIntentDTO;
		if (!text.isEmpty()) {
			List<NLULiteral> nluLiterals = getLiteral(text, dataBean);

			if (nluLiterals == null || nluLiterals.size() == 0) {
				return null;
			}

			NLULiteral nluLiteral = nluLiterals.get(0);
			nluIntentDTO = analyze(nluLiteral, dataBean, timeZone);
		} else {
			nluIntentDTO = new NluIntentDTO();
			nluIntentDTO.setIntent(NLUAnalyzerUtils.COMMAND_UNSPECIFIED);
		}

		return nluIntentDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.directv.nlu.discovery.analyzer.service.AnalyzerService#getLiteral(
	 * java.lang.String,
	 * com.directv.nlu.discovery.analyzer.dataloader.NluDataBean)
	 */
	public List<NLULiteral> getLiteral(String text, NluDataBean bean) {

		// the text value should be checked with null and empty value
		if (StringUtils.isBlank(text)) {
			return null;
		}

		Prediction prediction = bean.getPrediction();

		// normalize value
		text = NLUAnalyzerUtils.normalizeString(text);
		// convert the value to lower case
		text = text.toLowerCase();

		List<Feature> dataLookups = bean.getLookupService().getLookFeature(text);

		return prediction.execute(dataLookups);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.directv.nlu.discovery.analyzer.service.AnalyzerService#analyze
	 * (com.directv.nlu.tagger.dto.NLULiteral,
	 * com.directv.nlu.discovery.analyzer.dataloader.NluDataBean,
	 * java.lang.String)
	 */
	public NluIntentDTO analyze(NLULiteral nluLiteral, NluDataBean dataBean, String timeZone) {
		NluIntentDTO nluIntentDTO = new NluIntentDTO();

		if (nluLiteral == null) {
			return nluIntentDTO;
		}

		List<LiteralTag> literalTags = nluLiteral.getLiteralTags();

		if (literalTags == null || literalTags.size() == 0) {
			return nluIntentDTO;
		}

		/** set value to nluIntentDTOs **/
		nluIntentDTO.setLiteral(nluLiteral.toString());

		/** set full text **/
		nluIntentDTO.setText(NLUAnalyzerUtils.normalizeString(context.getHelper().getFullText(nluLiteral)));

		List<ProcessingStrategy> strategyFlow = context.getStrategyFlow(literalTags, dataBean);

		for (ProcessingStrategy strategy : strategyFlow) {
			strategy.execute(nluIntentDTO, nluLiteral, timeZone);
		}

		return nluIntentDTO;
	}

}

/*
 * NLULiteral.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.tagger.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.demo.voice.nlu.util.NLUAnalyzerUtils;
import com.directv.nlu.lookup.common.dto.Feature;
/**
 *
 * The Class NLULiteral.java
 *
 * @author Dung Tran
 *
 */
public class NLULiteral implements Serializable {

	private static final long serialVersionUID = 4635133118355789173L;
	private String confidenceValue;
	private List<LiteralTag> literalTags;
	private List<Feature> labelFeatures = null;

	public NLULiteral(){

	}

	public String getConfidenceValue() {
		return confidenceValue;
	}

	public void setConfidenceValue(String confidenceValue) {
		this.confidenceValue = confidenceValue;
	}

	public List<LiteralTag> getLiteralTags() {
		return literalTags;
	}

	public void setLiteralTags(List<LiteralTag> literalTags) {
		this.literalTags = literalTags;
	}

	public List<Feature> getLabelFeatures() {
		if (labelFeatures == null || labelFeatures.size() == 0) {
			return new ArrayList<Feature>();
		}

		return labelFeatures;
	}

	public void setLabelFeatures(List<Feature> labelFeatures) {
		if (labelFeatures == null || labelFeatures.size() == 0) {
			labelFeatures = new ArrayList<Feature>();
		}

		this.labelFeatures = labelFeatures;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		if (literalTags == null || literalTags.size() == 0) {
			return stringBuffer.toString();
		}

		for (LiteralTag literalTag : literalTags) {
			stringBuffer.append(NLUAnalyzerUtils.STRING_CORMA_START + literalTag.getTaggedName() + NLUAnalyzerUtils.STRING_CORMA_CLOSE + " ");
			stringBuffer.append(literalTag.getValue() + " ");
			stringBuffer.append(NLUAnalyzerUtils.STRING_CORMA_END + literalTag.getTaggedName() + NLUAnalyzerUtils.STRING_CORMA_CLOSE + " ");
		}

		return NLUAnalyzerUtils.normalizeString(stringBuffer.toString().trim());
	}
}

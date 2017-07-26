/*
 * AnalyzerService.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.service;

import java.util.List;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.tagger.dto.NLULiteral;

// TODO: Auto-generated Javadoc
/**
 * The Interface AnalyzerService.
 * 
 * @author tttran
 */
public interface AnalyzerService {

   /**
	 * Analyze.
	 * 
	 * @param nluLiterals
	 *            the nlu literals
	 * @param dataBean
	 *            the data bean
	 * @param timeZone
	 *            the time zone
	 * @return the nlu intent dto
	 */
   public NluIntentDTO analyze(List<NLULiteral> nluLiterals,
         NluDataBean dataBean, String timeZone);

   /**
	 * Analyze.
	 * 
	 * @param nluLiteral
	 *            the nlu value
	 * @param dataBean
	 *            the data bean
	 * @param timeZone
	 *            the time zone
	 * @return the nlu intent dto
	 */
   public NluIntentDTO analyze(NLULiteral nluLiteral, NluDataBean dataBean,
         String timeZone);

   /**
	 * Analyze.
	 * 
	 * @param text
	 *            the text
	 * @param dataBean
	 *            the data bean
	 * @param timeZone
	 *            the time zone
	 * @return the nlu intent dto
	 */
   public NluIntentDTO analyze(String text, NluDataBean dataBean, String timeZone);

   /**
	 * Gets the value.
	 * 
	 * @param text
	 *            the text
	 * @param bean
	 *            the bean
	 * @return the value
	 */
   public List<NLULiteral> getLiteral(String text, NluDataBean bean);
}

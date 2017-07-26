/*
 * ProcessingStrategy.java
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

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.MentionType;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;

/**
 * The Class ProcessingStrategy.java.
 *
 * @author Dung Tran
 *
 */
public interface ProcessingStrategy {
	
   public LabelTags[] getTags();

   public MentionType getMentionType();

   public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral, String timeZone);

   public void setValue(LabelTags tag, String value);

   public ProcessingStrategy getInstance(NluDataBean dataBean, ProcessingStrategyHelper helper);
   
   public void setOrder(int order);

   public int getOrder();

   public boolean isPostStrategy();

   public boolean isAlwaysRequired();
}

/*
 * MediaTypeProcessingStrategy.java
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
import com.demo.voice.nlu.util.NLUAnalyzerUtils;

/**
 * The Class MediaTypeProcessingStrategy.
 * 
 * @author tttran
 */
public class MediaTypeProcessingStrategy extends AbstractProcessingStrategy
      implements ProcessingStrategy {

   private static final LabelTags[] tags = { LabelTags.Tv, LabelTags.Movie,
         LabelTags.SportEvent, LabelTags.SportGenre, LabelTags.SportRelated,
         LabelTags.League, LabelTags.Team, LabelTags.Season, LabelTags.Episode };

   private static final MentionType mentionType = MentionType.MEDIA_TYPE;

   protected boolean sportType = false;
   protected boolean movieType = false;
   protected boolean tvType = false;

   //
   // protected List<String> sportGenres = null;

   public MediaTypeProcessingStrategy() {
      super();
   }

   public MediaTypeProcessingStrategy(NluDataBean dataBean,
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

   /*
    * (non-Javadoc)
    *
    * @see com.directv.nlu.analyzer.processor.strategy.ProcessingStrategy#
    * getInstance(com.directv.nlu.analyzer.dataloader.NluDataBean,
    * com.directv
    * .diva.nlu.analyzer.processor.strategy.helper.ProcessingStrategyHelper)
    */
   @Override
   public ProcessingStrategy getInstance(NluDataBean dataBean,
         ProcessingStrategyHelper helper) {
      return new MediaTypeProcessingStrategy(dataBean, helper);
   }
   
   /*
    * (non-Javadoc)
    *
    * @see
    * com.directv.nlu.analyzer.processor.ProcessingStrategy#setValue(com
    * .directv.diva.nlu.tagger.enums.LabelTags, java.lang.String)
    */
   @Override
   public void setValue(LabelTags tag, String value) {
      switch (tag) {
      case Tv:
      case Episode:
      case Season:
         tvType = true;
         break;
      case Movie:
         movieType = true;
         break;
      case SportEvent:
      case SportGenre:
      case SportRelated:
      case League:
      case Team:
         sportType = true;
         break;
      default:
         break;
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see
    * com.directv.nlu.analyzer.processor.ProcessingStrategy#execute(com
    * .directv.diva.nlu.analyzer.dto.NluIntentDTO,
    * com.directv.nlu.tagger.dto.NLULiteral)
    */
   @Override
   public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral,
         String timeZone) {

	   if (movieType) {
		   intent.setMediaType(NLUAnalyzerUtils.MOVIES);
	   } else if (tvType) {
		   intent.setMediaType(NLUAnalyzerUtils.TV);
	   } else if (sportType) {
		   intent.setMediaType(NLUAnalyzerUtils.SPORTS);
	   }

      return intent;
   }
}

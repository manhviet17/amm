/*
 * GenreProcessingStrategy.java
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

import org.apache.commons.collections.CollectionUtils;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.MentionType;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;
import com.directv.nlu.lookup.common.dto.Feature;
	
/**
 * The Class GenreProcessingStrategy.
 *
 * @author tttran
 */
public class GenreProcessingStrategy extends AbstractProcessingStrategy
      implements ProcessingStrategy {

   private static final LabelTags[] tags = { LabelTags.Genre };

   private static final MentionType mentionType = MentionType.GENRE;

   protected List<String> genres = null;

   public GenreProcessingStrategy() {
      super();
   }

   public GenreProcessingStrategy(NluDataBean dataBean,
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
      return new GenreProcessingStrategy(dataBean, helper);
   }

   @Override
   public void setValue(LabelTags tag, String value) {
      if (LabelTags.Genre.equals(tag)) {
         if (genres == null) {
            genres = new ArrayList<String>();
         }

         NLUAnalyzerUtils.addNoNullNoDups(genres, value);
      }
   }

   @Override
   public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral,
         String timeZone) {

      List<String> unnormalized = new ArrayList<String>();

      if (CollectionUtils.isNotEmpty(this.genres)) {
         String processedGenres = processGenre(this.genres,
               nluLiteral.getLabelFeatures(), unnormalized);
         intent.setGenre(this.helper.addTo(intent.getGenre(), processedGenres,
               ","));
      }


      return intent;
   }

   private String processGenre(List<String> genre, List<Feature> labelFeatures,
	         List<String> unnormalized) {

	      List<String> savedGenres = new ArrayList<String>();
//	      for (int i = 0; i < genre.size(); i++) {
//	         String valueGenre = genre.get(i);
//	         // process here with genre has more two genre
//	         double rate = this.helper.countRateMatching(valueGenre, labelFeatures,
//	               new StringBuffer(), TypeIndex.genre);
//	         if (rate >= 1.0) {
////
////	            this.helper.normalize(MentionType.GENRE, valueGenre, savedGenres,
////	                  unnormalized);
//	        	 savedGenres.add(valueGenre);
//
//	         } else {
//	            String[] split = valueGenre.split(" ");
//	            for (int j = 0; j < split.length; j++) {
//
////	               this.helper.normalize(MentionType.GENRE, split[j], savedGenres,
////	                     unnormalized);
//	            	savedGenres.add(split[j]);
//
//	            }
//	         }
//
//	      }

	      return this.helper.toCsv(savedGenres);

	   }
}

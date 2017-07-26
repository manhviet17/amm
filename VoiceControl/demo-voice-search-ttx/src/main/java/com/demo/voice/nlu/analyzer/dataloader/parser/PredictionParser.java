/*
 * PredictionParser.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.dataloader.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.voice.nlu.tagger.processor.Prediction;
import com.directv.eds.common.fileprocessor.parser.FileParser;
import com.directv.nlu.crf.model.Model;
/**
 *
 * The Class PredictionParser.java.
 *
 * @author Dung Tran
 *
 */
public class PredictionParser implements FileParser<Prediction> {

   protected static final Logger LOGGER = LoggerFactory
         .getLogger(PredictionParser.class.getName());

   private static final String TYPE = "txt";

   @Override
   public String getType() {
      return TYPE;
   }

   @Override
   public List<Map<String, Prediction>> getFullListingObjectsFromFile(
         String fileName, Map<String, Boolean> nvMap) throws IOException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Map<String, Prediction> getUpdateListingObjectsFromFile(
         String fileName, Map<String, Boolean> nvMap) throws IOException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Prediction> getSimpleObjectsFromFile(String fileName)
         throws IOException {
      List<Prediction> results = new ArrayList<Prediction>();
      Prediction prediction = new Prediction();
      Model model = new Model();
      LOGGER.info("Loading model..");
      model.loadModel(fileName);
      prediction.setModel(model);
      results.add(prediction);

      return results;
   }

}

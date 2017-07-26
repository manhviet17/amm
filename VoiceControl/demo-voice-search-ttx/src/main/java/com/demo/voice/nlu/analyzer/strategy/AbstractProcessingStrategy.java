/*
 * AbstractProcessingStrategy.java
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

import java.util.List;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.directv.nlu.lookup.common.dto.Feature;

/**
 *
 * The Class AbstractProcessingStrategy.java.
 *
 * @author Dung Tran
 *
 */
public abstract class AbstractProcessingStrategy implements ProcessingStrategy {

   protected NluDataBean dataBean = null;

   protected List<Feature> features = null;

   protected int order = 0;

   protected ProcessingStrategyHelper helper = null;

   public AbstractProcessingStrategy() {
   }

   public AbstractProcessingStrategy(NluDataBean dataBean,
         ProcessingStrategyHelper helper) {
      setHelper(helper);
      setDataBean(dataBean);
   }

   /**
    * @param dataBean the dataBean to set
    */
   public void setDataBean(NluDataBean dataBean) {
      this.dataBean = dataBean;
      if (this.helper == null) {
         this.helper = new ProcessingStrategyHelper();
      }
      helper.setDataBean(dataBean);
   }

   /**
    * @return the order
    */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	public void setHelper(ProcessingStrategyHelper helper) {
		this.helper = helper;
	}

	public boolean isPostStrategy() {
		return false;
	}

	public boolean isAlwaysRequired() {
		return false;
	}

	public ProcessingStrategyHelper getHelper() {
		return helper;
	}
}

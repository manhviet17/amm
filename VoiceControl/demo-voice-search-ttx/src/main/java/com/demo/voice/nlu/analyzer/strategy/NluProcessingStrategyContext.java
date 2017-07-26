/*
 * NluProcessingStrategyContext.java
 *
 * Copyright (c) 2013 DIRECTV, Inc.
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections.CollectionUtils;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.MentionType;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.demo.voice.nlu.tagger.dto.LiteralTag;
import com.demo.voice.nlu.tagger.enums.LabelTags;

/**
 * The Class NluProcessingStrategyContext.
 * 
 * @author tttran
 */
public class NluProcessingStrategyContext {

	protected ConcurrentMap<LabelTags, List<ProcessingStrategy>> tagsToStrategyMap;

	protected List<ProcessingStrategy> requiredStrategies;

	protected ProcessingStrategyHelper helper = new ProcessingStrategyHelper();

	protected ProcessingStrategyComparator strategyComparator = new ProcessingStrategyComparator();

	public NluProcessingStrategyContext() {
	}

	public NluProcessingStrategyContext(List<ProcessingStrategy> strategies) {
		this.setStrategies(strategies);
	}

	public NluProcessingStrategyContext(List<ProcessingStrategy> strategies, List<ProcessingStrategy> postStrategies) {
		this.setStrategies(strategies);
		this.setStrategiesPost(postStrategies);
	}

	/**
	 * @return the tagsToStrategyMap
	 */
	public Map<LabelTags, List<ProcessingStrategy>> getTagsToStrategyMap() {
		return tagsToStrategyMap;
	}

	/**
	 * @return the helper
	 */
	public ProcessingStrategyHelper getHelper() {
		return helper;
	}

	/**
	 * @param helper
	 *            the helper to set
	 */
	public void setHelper(ProcessingStrategyHelper helper) {
		this.helper = helper;
	}

	/**
	 * This adds to the strategy map, a list of strategies that are applied at
	 * the beginning.
	 *
	 * @param strategies
	 *            the strategies to set
	 */
	public void setStrategies(List<ProcessingStrategy> strategies) {

		if (tagsToStrategyMap == null) {
			this.tagsToStrategyMap = new ConcurrentHashMap<LabelTags, List<ProcessingStrategy>>(16, 0.9f, 3);
		}

		this.buildMap(strategies, false);
	}

	/**
	 * This adds to the strategy map, a list of strategies that are applied at
	 * the end after all other non-"post" strategies have executed.
	 * 
	 * @param strategies
	 */
	public void setStrategiesPost(List<ProcessingStrategy> strategies) {

		if (tagsToStrategyMap == null) {
			this.tagsToStrategyMap = new ConcurrentHashMap<LabelTags, List<ProcessingStrategy>>(16, 0.9f, 3);
		}

		this.buildMap(strategies, true);
	}

	/**
	 * Adds strategies to map. If post is true only strategies where
	 * isPostStrategy is true are added. Else if post is false then only
	 * strategies where isPostStrategy is false are added.
	 * 
	 * @param strategies
	 * @param post
	 */
	private synchronized void buildMap(List<ProcessingStrategy> strategies, boolean post) {
		if (CollectionUtils.isEmpty(strategies)) {
			// TODO throw loading exception
			return;
		}

		int order = 0;

		if (post) {
			order = Integer.MAX_VALUE - strategies.size() - 1;
		}

		for (ProcessingStrategy strategy : strategies) {
			if (strategy.isAlwaysRequired()) {
				if (requiredStrategies == null) {
					requiredStrategies = new ArrayList<ProcessingStrategy>();
				}

				if (this.getStrategy(strategy.getMentionType(), requiredStrategies) == null) {
					requiredStrategies.add(strategy);
				}
			}

			if (post && !strategy.isPostStrategy()) {
				continue;
			} else if (!post && strategy.isPostStrategy()) {
				continue;
			}

			LabelTags[] tags = strategy.getTags();

			strategy.setOrder(order++);

			if (tags == null) {
				// TODO log warning
				continue;
			} else {
				for (LabelTags tag : tags) {
					this.addToMap(tag, strategy);
				}
			}
		}

	}

	/**
	 *
	 * @param tag
	 * @param strategy
	 */
	private void addToMap(LabelTags tag, ProcessingStrategy strategy) {
		List<ProcessingStrategy> strategyList = this.tagsToStrategyMap.get(tag);

		if (strategyList == null) {
			strategyList = Collections.synchronizedList(new ArrayList<ProcessingStrategy>());
			List<ProcessingStrategy> tempList = this.tagsToStrategyMap.putIfAbsent(tag, strategyList);
			if (tempList != null) {
				strategyList = tempList;
			}
		}

		if (this.getStrategy(strategy.getMentionType(), strategyList) == null) {
			strategyList.add(strategy);
		}
	}

	/**
	 * Based on the order of the value tags brought in and the order of the
	 * strategies used in creating the strategy map, this function dynamically
	 * generates a work flow of strategies to execute.
	 *
	 * @param literalTags
	 * @param dataBean
	 * @return
	 */
	public List<ProcessingStrategy> getStrategyFlow(List<LiteralTag> literalTags, NluDataBean dataBean) {
		if (CollectionUtils.isEmpty(literalTags)) {
			return Collections.emptyList();
		}

		List<ProcessingStrategy> strategyFlow = new ArrayList<ProcessingStrategy>();

		for (LiteralTag litTag : literalTags) {
			LabelTags labelTag = LabelTags.valueOf(litTag.getTaggedName());
			String value = (litTag.getValue() != null) ? litTag.getValue().trim() : null;

			List<ProcessingStrategy> mapValues = this.tagsToStrategyMap.get(labelTag);

			if (!CollectionUtils.isEmpty(mapValues)) {

				for (ProcessingStrategy tempStrategy : mapValues) {
					ProcessingStrategy strategyInstance = this.getStrategy(tempStrategy.getMentionType(), strategyFlow);

					// if (strategyInstance == null) {
					strategyInstance = tempStrategy.getInstance(dataBean, this.helper);
					strategyInstance.setOrder(tempStrategy.getOrder());
					strategyFlow.add(strategyInstance);
					// }

					strategyInstance.setValue(labelTag, value);
				}
			}
		}

		Collections.sort(strategyFlow, strategyComparator);

		if (CollectionUtils.isNotEmpty(requiredStrategies)) {
			for (ProcessingStrategy tempStrategy : requiredStrategies) {
				ProcessingStrategy strategyInstance = this.getStrategy(tempStrategy.getMentionType(), strategyFlow);

				if (strategyInstance == null) {
					strategyInstance = tempStrategy.getInstance(dataBean, this.helper);
					strategyInstance.setOrder(tempStrategy.getOrder());
					strategyFlow.add(strategyInstance);
				}
			}
		}

		return strategyFlow;
	}

	/**
	 * Returns the strategy in a list given the mention type of that strategy.
	 *
	 * @param mentionType
	 * @param strategies
	 * @return
	 */
	private ProcessingStrategy getStrategy(MentionType mentionType, List<ProcessingStrategy> strategies) {
		for (ProcessingStrategy strategy : strategies) {
			if (mentionType.equals(strategy.getMentionType())) {
				return strategy;
			}
		}

		return null;
	}

	protected class ProcessingStrategyComparator implements Comparator<ProcessingStrategy> {
		public int compare(ProcessingStrategy f1, ProcessingStrategy f2) {
			return Integer.valueOf(f1.getOrder()).compareTo(Integer.valueOf(f2.getOrder()));
		}
	}
}

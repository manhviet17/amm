/*
 * IntentProcessingStrategy.java
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

import java.util.concurrent.ThreadLocalRandom;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.MentionType;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.strategy.helper.ProcessingStrategyHelper;
import com.demo.voice.nlu.tagger.dto.NLULiteral;
import com.demo.voice.nlu.tagger.enums.LabelTags;
import com.demo.voice.nlu.util.NLUAnalyzerUtils;

/**
 * The Class IntentProcessingStrategy.
 *
 * @author tttran
 */
public class IntentProcessingStrategy extends AbstractProcessingStrategy implements ProcessingStrategy {

	private static final LabelTags[] tags = { LabelTags.CommandBuy, LabelTags.CommandPlay, LabelTags.Time,
			LabelTags.Season, LabelTags.Episode, LabelTags.CommandRecommend, LabelTags.CommandRecord,
			LabelTags.GenericOrder, LabelTags.CommandSearch, LabelTags.Genre, LabelTags.SportGenre,
			LabelTags.MusicGenre, LabelTags.Title, LabelTags.Station, LabelTags.Delivery, LabelTags.ChannelNumber,
			LabelTags.Credit, LabelTags.Command, LabelTags.Name, LabelTags.UserId, LabelTags.Confirm,
			LabelTags.ConfirmFalse, LabelTags.ConfirmTrue, LabelTags.Code, LabelTags.Delivery };

	private static final MentionType mentionType = MentionType.INTENT;

	private static final boolean post = true;

	private static final boolean alwaysRequired = false;

	protected boolean commandStation = false;
	protected boolean station = false;
	protected boolean channelNumber = false;
	protected boolean hasCredit = false;
	protected String strCommand = null;
	// protected String delivery = null;
	protected String commandValue = null; // to save the value of command

	public IntentProcessingStrategy() {
		super();
	}

	public IntentProcessingStrategy(NluDataBean dataBean, ProcessingStrategyHelper helper) {
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
	 * @see com.directv.nlu.analyzer.processor.ProcessingStrategy#getInstance
	 * (com.directv.nlu.analyzer.dataloader.NluDataBean)
	 */
	@Override
	public ProcessingStrategy getInstance(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		return new IntentProcessingStrategy(dataBean, helper);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.directv.nlu.analyzer.processor.ProcessingStrategy#setValue(com
	 * .directv.diva.nlu.tagger.enums.LabelTags, java.lang.String)
	 */
	@Override
	public void setValue(LabelTags tag, String value) {
		// if command is the first command, value of this command will be set
		// for strCommand and commandValue fields in intent
		// else do nothing
		// because the first command will take priority in returned intent
		if (strCommand == null) {
			switch (tag) {
			case Name:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.GREETING;
				break;
			case UserId:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.INPUT_USERID;
				break;
			case Confirm:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.REQUEST_RESET;
				break;
			case ConfirmTrue:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.CONFIRM_TRUE;
				break;
			case ConfirmFalse:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.CONFIRM_FALSE;
				break;
			case Code:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.INPUT_CODE;
				break;
			case Delivery:
				commandValue = value;
				strCommand = NLUAnalyzerUtils.SEND_CODE;
				break;
			default:
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.directv.nlu.analyzer.processor.ProcessingStrategy#execute(com
	 * .directv.diva.nlu.analyzer.dto.NluIntentDTO,
	 * com.directv.nlu.tagger.dto.NLULiteral)
	 */
	@Override
	public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral, String timeZone) {

		// if (strCommand == null && !station) { -- unaccepted change
		if (intent.getIntent() != null && intent.getIntent().equals(NLUAnalyzerUtils.COMMAND_UNSPECIFIED))
			return intent;
		if (strCommand == null) {
			intent.setIntent(NLUAnalyzerUtils.COMMAND_UNSPECIFIED);
		} else {
			// this is a post strategy because it combines usage of values from
			// the value tag and values that should be set in the NLU intent
			// intent.setIntent(this.processIntent(intent.getMediaType(),
			// delivery, strCommand));
			if (intent.getIntent() != null && !intent.getIntent().isEmpty()) {
				if ((intent.getIntent().equals(NLUAnalyzerUtils.REQUEST_RESET)
						&& this.processIntent().equals(NLUAnalyzerUtils.INPUT_USERID))
						|| (intent.getIntent().equals(NLUAnalyzerUtils.INPUT_USERID)
								&& this.processIntent().equals(NLUAnalyzerUtils.REQUEST_RESET))) {
					intent.setIntent(NLUAnalyzerUtils.REQUEST_RESET_USERID);
				}

			} else {
				intent.setIntent(this.processIntent());
			}
		}

		return intent;
	}

	/**
	 *
	 * @param mediaTypeValue
	 * @param valueOfDelivery
	 * @param valueOfCommand
	 * @return
	 */

	private String processIntent() {

		// check send code by email intent
		if (NLUAnalyzerUtils.hasIntentValue(NLUAnalyzerUtils.EMAIL_INTENT_VALUE, commandValue)) {
			return NLUAnalyzerUtils.SEND_CODE_EMAIL;
		}
		// check send code by sms intent
		if (NLUAnalyzerUtils.hasIntentValue(NLUAnalyzerUtils.SMS_INTENT_VALUE, commandValue)) {
			return NLUAnalyzerUtils.SEND_CODE_SMS;
		}
		// check send code by phonecall intent
		if (NLUAnalyzerUtils.hasIntentValue(NLUAnalyzerUtils.PHONECALL_INTENT_VALUE, commandValue)) {
			return NLUAnalyzerUtils.SEND_CODE_PHONECALL;
		}
		// check chating intent
		if (NLUAnalyzerUtils.hasIntentValue(NLUAnalyzerUtils.CHATING_INTENT_VALUE, commandValue)) {
			// the intent should be returned "hi,hello"
			int randomNum = ThreadLocalRandom.current().nextInt(0, NLUAnalyzerUtils.CHATING_INTENT_VALUE.length);
			return NLUAnalyzerUtils.CHATING_INTENT_VALUE[randomNum];
		}
		if (strCommand.equals(NLUAnalyzerUtils.INPUT_USERID) && isNumeric(commandValue))
			return NLUAnalyzerUtils.INPUT_CODE;
		return strCommand;
	}

	public boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	@Override
	public boolean isPostStrategy() {
		return post;
	}

	@Override
	public boolean isAlwaysRequired() {
		return alwaysRequired;
	}

}

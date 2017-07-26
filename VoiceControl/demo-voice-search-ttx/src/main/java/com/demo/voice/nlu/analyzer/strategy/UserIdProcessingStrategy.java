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

public class UserIdProcessingStrategy extends AbstractProcessingStrategy implements ProcessingStrategy {

	private static final LabelTags[] tags = { LabelTags.UserId };
	
	private static final MentionType mentionType = MentionType.USERID;
	
	protected List<String> userIds = null;
	

	public UserIdProcessingStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserIdProcessingStrategy(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		super(dataBean, helper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public LabelTags[] getTags() {
		// TODO Auto-generated method stub
		return tags;
	}

	@Override
	public MentionType getMentionType() {
		// TODO Auto-generated method stub
		return mentionType;
	}

	@Override
	public NluIntentDTO execute(NluIntentDTO intent, NLULiteral nluLiteral, String timeZone) {
		// TODO Auto-generated method stub
		//List<String> unnormalized = new ArrayList<String>();

		if (CollectionUtils.isNotEmpty(this.userIds)) {
			intent.setUserId(userIds.get(0));
		}
		return intent;
	}

	@Override
	public void setValue(LabelTags tag, String value) {
		if (LabelTags.UserId.equals(tag)) {
			if (userIds == null) {
				userIds = new ArrayList<String>();
	         }
	         NLUAnalyzerUtils.addNoNullNoDups(userIds, value);
		}

	}

	@Override
	public ProcessingStrategy getInstance(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		// TODO Auto-generated method stub
		return new UserIdProcessingStrategy(dataBean,helper);
	}

}

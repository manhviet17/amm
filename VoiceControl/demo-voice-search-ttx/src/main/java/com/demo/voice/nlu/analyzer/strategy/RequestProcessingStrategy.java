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

public class RequestProcessingStrategy extends AbstractProcessingStrategy implements ProcessingStrategy {
	private static final LabelTags[] tags = { LabelTags.Confirm };
	private static final MentionType mentionType = MentionType.CONFIRM;

	private List<String> confirms = null;

	public RequestProcessingStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestProcessingStrategy(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		super(dataBean, helper);
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
		if (CollectionUtils.isNotEmpty(this.confirms)) {
			intent.setRequest(confirms.get(0));
		}
		return intent;
	}

	@Override
	public void setValue(LabelTags tag, String value) {
		// TODO Auto-generated method stub
		if (LabelTags.Confirm.equals(tag)) {
			if (confirms == null) {
				confirms = new ArrayList<String>();
			}
			NLUAnalyzerUtils.addNoNullNoDups(confirms, value);
		}
	}

	@Override
	public ProcessingStrategy getInstance(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		// TODO Auto-generated method stub
		return new RequestProcessingStrategy(dataBean, helper);
	}

}

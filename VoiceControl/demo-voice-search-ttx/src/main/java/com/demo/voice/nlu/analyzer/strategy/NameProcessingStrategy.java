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

public class NameProcessingStrategy extends AbstractProcessingStrategy implements ProcessingStrategy {

	private static final LabelTags[] tags = { LabelTags.Name };

	private static final MentionType mentionType = MentionType.NAME;

	protected List<String> names = null;

	public NameProcessingStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NameProcessingStrategy(NluDataBean dataBean, ProcessingStrategyHelper helper) {
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

		if (CollectionUtils.isNotEmpty(this.names)) {
			intent.setName(names.get(0));
		}
		return intent;
	}

	@Override
	public void setValue(LabelTags tag, String value) {
		// TODO Auto-generated method stub
		if (LabelTags.Name.equals(tag)) {
			if (names == null) {
				names = new ArrayList<String>();
			}
			NLUAnalyzerUtils.addNoNullNoDups(names, value);
		}
	}

	@Override
	public ProcessingStrategy getInstance(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		// TODO Auto-generated method stub
		return new NameProcessingStrategy(dataBean, helper);
	}

}

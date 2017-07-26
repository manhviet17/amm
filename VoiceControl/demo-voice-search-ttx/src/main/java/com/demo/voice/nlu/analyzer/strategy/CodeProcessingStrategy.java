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

public class CodeProcessingStrategy extends AbstractProcessingStrategy implements ProcessingStrategy {

	private static final LabelTags[] tags = { LabelTags.Code };

	private static final MentionType mentionType = MentionType.CODE;

	protected List<String> codes = null;
	
	
	public CodeProcessingStrategy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CodeProcessingStrategy(NluDataBean dataBean, ProcessingStrategyHelper helper) {
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
		if (CollectionUtils.isNotEmpty(this.codes)) {
			intent.setCode(codes.get(0));
		}
		return intent;
	}

	@Override
	public void setValue(LabelTags tag, String value) {
		// TODO Auto-generated method stub
		if (LabelTags.Code.equals(tag)) {
			if (codes == null) {
				codes = new ArrayList<String>();
			}
			NLUAnalyzerUtils.addNoNullNoDups(codes, value);
		}
	}

	@Override
	public ProcessingStrategy getInstance(NluDataBean dataBean, ProcessingStrategyHelper helper) {
		// TODO Auto-generated method stub
		return new CodeProcessingStrategy(dataBean, helper);
	}

}

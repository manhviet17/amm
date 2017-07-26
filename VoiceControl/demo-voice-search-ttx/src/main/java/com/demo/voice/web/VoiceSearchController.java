package com.demo.voice.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.service.AnalyzerService;
import com.demo.voice.process.dto.Response;
import com.demo.voice.process.service.Service;
import com.demo.voice.process.utils.Normalize;

@Controller
@RequestMapping("/service")
public class VoiceSearchController {

	/** The logger. */
	private static Logger logger = Logger.getLogger("controller");

	private AnalyzerService analyzerService;
	private NluDataBean dataBean;

	private Service service;
	private Normalize normalize;

	private final String TIME_ZONE = "";

	@RequestMapping(value = "/nlu", method = RequestMethod.GET)
	public @ResponseBody Response processNluTagging(@RequestParam String text, @RequestParam String uid) {
		logger.info("Input Text : " + text);
		logger.info("Device Id : " + uid);
		Response response = new Response();
		NluIntentDTO currentIntentDTO = analyzerService.analyze(text, dataBean, TIME_ZONE);
		logger.info("Literal : " + currentIntentDTO.getLiteral());
		logger.info("Intent: " + currentIntentDTO.getIntent());

		// call Search service here.
		// Response response = service.nlp(currentIntentDTO.getLiteral(),
		// uid);
		response = service.fsm(currentIntentDTO, uid);
		response.setText(text);
		response.setLiteral(currentIntentDTO.getLiteral());

		return response;
	}

	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public @ResponseBody String resetConversation(@RequestParam String uid) {
		return String.valueOf(service.resetConversation(uid));
	}

	public AnalyzerService getAnalyzerService() {
		return analyzerService;
	}

	public void setAnalyzerService(AnalyzerService analyzerService) {
		this.analyzerService = analyzerService;
	}

	public NluDataBean getDataBean() {
		return dataBean;
	}

	public void setDataBean(NluDataBean dataBean) {
		this.dataBean = dataBean;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void setNormalize(Normalize normalize) {
		this.normalize = normalize;
	}

}

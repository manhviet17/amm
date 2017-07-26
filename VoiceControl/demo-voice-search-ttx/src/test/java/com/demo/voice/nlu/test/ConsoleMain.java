/*
 * ConsoleMain.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */

package com.demo.voice.nlu.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.demo.voice.nlu.analyzer.dataloader.NluDataBean;
import com.demo.voice.nlu.analyzer.dto.CanonicalDTO;
import com.demo.voice.nlu.analyzer.dto.CanonicalElementDTO;
import com.demo.voice.nlu.analyzer.dto.NluIntentDTO;
import com.demo.voice.nlu.analyzer.dto.TitleDTO;
import com.demo.voice.nlu.analyzer.service.AnalyzerService;

public class ConsoleMain {
	protected static final Logger logger = LoggerFactory
			.getLogger(ConsoleMain.class);

	private ClassPathXmlApplicationContext ctx = null;

	public ClassPathXmlApplicationContext getContext() {
		return ctx;
	}

	public ConsoleMain() {
		ctx = new ClassPathXmlApplicationContext("voice-search-context.xml");
	}

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception ex) {
			return "ERROR";
		}
	}

	public static String getTimezoneOffsetFromUTC(String clientTimeZone) {
		return getTimezoneOffsetFromUTC(new DateTime(
				DateTimeZone.forID(clientTimeZone)));
	}

	public static String getTimezoneOffsetFromUTC(DateTime curr) {
		String offset = "+0000";

		if (curr != null) {
			DateTimeFormatter timeFormatter = ISODateTimeFormat.dateTime();
			String tempTime = timeFormatter.print(curr);

			if (!tempTime.contains("Z"))
				offset = tempTime.substring(tempTime.length() - 6,
						tempTime.length());

		}
		return offset.replace(":", "");
	}

	public static void main(String[] args) throws IOException {

		// List<String> characters = new ArrayList<String>();
		// characters.add("O");
		// characters.add("A");
		// characters.add("V");
		// characters.add("P");
		// characters.add("B");
		// characters.add("N");
		// characters.add("V");
		// characters.add("I");
		// characters.add("U");
		// characters.add("C");
		// characters.add("A");
		// characters.add("H");

		ConsoleMain console = new ConsoleMain();

		ClassPathXmlApplicationContext ctx = console.getContext();

//		DiscoveryService service = (DiscoveryService) ctx
//				.getBean("nluDiscoveryService");
		AnalyzerService service = (AnalyzerService) ctx.getBean("analyzerService");

		NluDataBean dataBean = (NluDataBean) ctx.getBean("dataBean");
//
//		System.out.println(console.getHostName());
//		System.out.println("Version: " + dataBean.getCompleteVersion());
		System.out.println("Date: "
				+ (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z"))
						.format(Calendar.getInstance().getTime()));

		String defaultTimeZone = "+0700";

		System.out.println("Test time 1" + getTimezoneOffsetFromUTC("PST8PDT"));
		System.out.println("Test time 2" + getTimezoneOffsetFromUTC(defaultTimeZone));
		System.out.println("Test time 3" + DateTimeZone.forID("PST8PDT"));
		System.out.println("Test time 4" + DateTimeZone.forID(defaultTimeZone));


		console.testCRF(service, defaultTimeZone, dataBean);
	}

	// For Test
	public void testCRF(AnalyzerService service, String timeZone, NluDataBean dataBean) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Client TimeZone: " + timeZone);
		System.out.println("Enter text::");
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if ("quit!".equals(line)) {
				break;
			} else if (line == null || "".equals(line.trim())) {
				continue;
			} else {
				NluIntentDTO intent = service.analyze(line, dataBean, timeZone);
				print(intent);
			}
			
		}
//		// set context
//		NluContextDTO context = new NluContextDTO();
//		String text = "show me the notebook";
//
//		long start = System.currentTimeMillis();
//		String id = "TESTER_" + start;
//
//		NluIntentRowDTO intentRow = service.discover(text, context, null,
//				timeZone, true, id);
//		List<NluIntentRowDTO> intentRowList = new ArrayList<NluIntentRowDTO>();
//		intentRowList.add(intentRow);
//		context.setIntents(intentRowList);
//		// String fileName = "Literal_111.txt";
//		// String filePath = "/home/vinhnq/Desktop/";
//
//		while (scanner.hasNext()) {
//			String line = scanner.nextLine();
//			if ("quit!".equals(line)) {
//				break;
//			} else if (line == null || "".equals(line.trim())) {
//				continue;
//			} else {
//				// List<TitleDTO> titleDTOs =
//				// testingTmsId(intentRow.getCurrentIntent());
//				NluIntentRowDTO intentRowDTO = service.discover(line,
//						context, null, timeZone, true, id);
//
//				// List<NLULiteral> list = service.getLiteral(line);
//				// writeFile(list.get(0).toString(), fileName, filePath);
//				// logger.warn(list.get(0).toString());
//
//				intentRowList.add(intentRowDTO);
//				context.setIntents(intentRowList);
//				System.out.println("text: " + line);
//				print(context.getLastMergedIntent());
//			}
//		}
	}

	private static void writeFile(String line, String fileName, String filePath) {
		try {

			File file = new File(filePath + fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getAbsoluteFile(),
					true);
			BufferedWriter bw = new BufferedWriter(fileWritter);

			bw.write(line);
			bw.newLine();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<TitleDTO> testingTmsId(NluIntentDTO currentIntentDTO) {

		int tmsId = 1;
		List<TitleDTO> titleDTOs = new ArrayList<TitleDTO>();
		if (currentIntentDTO.getTitles() != null
				&& currentIntentDTO.getTitles().size() > 0) {
			List<CanonicalDTO> canonicalDTOs = currentIntentDTO.getTitles();
			for (CanonicalDTO canonicalDTO : canonicalDTOs) {
				for (CanonicalElementDTO canonicalElementDTO : canonicalDTO
						.getCanonicals()) {
					TitleDTO titleDTO = new TitleDTO();
					titleDTO.setTitle(canonicalElementDTO.getName());
					titleDTO.setTmsId("000" + tmsId);
					titleDTOs.add(titleDTO);
					tmsId++;
				}
			}
		}

		return titleDTOs;
	}

	private List<String> readFileSentences(String fileName) {

		List<String> sentences = new ArrayList<String>();

		BufferedReader br = null;
		String nextLine = "";

		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((nextLine = br.readLine()) != null) {
				sentences.add(nextLine);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sentences;
	}

	private void print(NluIntentDTO intentDTO) {
		String intent_str, titles, actors, stations = null, sportteams;

		titles = listToString(intentDTO.getTitles());

		actors = listToString(intentDTO.getCredits());

		intent_str = String.format("{\n" + "  intent: %s,\n" + "  text: %s,\n"
				+ "  value: %s,\n"
				+ "  mediaType: %s,\n" + "  genre: %s,\n"
				+ "  titles: %s,\n"
				+ "  credits: %s,\n}",
				intentDTO.getIntent(), intentDTO.getText(),
				intentDTO.getLiteral(),
				intentDTO.getMediaType(), intentDTO.getGenre(),
				titles,
				actors);
		System.out.println(intent_str);
	}

	private String listToString(List<CanonicalDTO> list) {
		String str = null;

		if (list != null) {
			str = "[";
			str += "\n";
			for (CanonicalDTO cn : list) {
				str += String
						.format("    {\n      text: %s,\n      value: %s,\n exactMatchFlag: %s",
								cn.getText(), cn.getValue(),
								cn.isExactMatchFlag());
				if (cn.getCanonicals() != null) {
					str += "\n      canonicals: [";
					for (CanonicalElementDTO ce : cn.getCanonicals()) {
						str += String.format("\n        name: %s,",
								ce.getName());
					}
					str += "   ]";
				}
				str += "\n    },\n";
			}
			str += "  ]\n";
		}

		return str;
	}
}

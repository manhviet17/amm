/*
 * NLUAnalyzerUtils.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.voice.nlu.tagger.enums.LabelTags;

/**
 *
 * The Class NLUAnalyzerUtils.java.
 *
 * @author Dung Tran
 * @author Vinh Nguyen
 *
 */
public final class NLUAnalyzerUtils {

	/**
	 * Instantiates a new NLU analyzer utils.
	 */
	private NLUAnalyzerUtils() {
	}

	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(NLUAnalyzerUtils.class);

	// -------------------------------------------VARIABLES-----------------------------------------
	// public static String STRING_BEGIN = "B_";
	public static String STRING_CORMA_END = "</"; // format value
	public static String STRING_CORMA_START = "<";
	public static String STRING_CORMA_CLOSE = ">";
	// public static String STRING_INSIDE = "I_";
	// public static String STRING_OTHER = "Other";
	// public static String DELIM = " ,:"; // using for token phrase

	public static String COMMAND_SEARCH = "search";
	public static String COMMAND_PLAY = "play";
	public static String COMMAND_BUY = "buy";
	public static String COMMAND_RECORD = "record";
	public static String COMMAND = "command";
	public static String COMMAND_RECOMMEND = "recommend";
	public static String COMMAND_UNSPECIFIED = "unspecified-na-na";
	public static String DEMAND = "demand";
	public static String ON_DEMAMD = "ondemand";
	public static String STREAM = "stream";
	public static String STREAMING = "streaming";
	public static String COMMAND_CHANNEL_STATION = "command-channel-station";
	public static String COMMAND_CHANNEL_CHANNEL = "command-channel-number";
	public static String COMMAND_INFO_PROGRAMMING = "info-program-na";
	public static String COMMAND_INFO_PERSON = "info-person-na";
	public static String COMMAND_START_OVER = "command-start_over-na";
	public static String COMMAND_HELP = "command-help-na";
	public static String COMMAND_BOOKMARK = "bookmark-na-add";
	public static String COMMAND_BOOKMARK_BOOKMARK = "bookmark-bookmark-add";
	public static String COMMAND_SHOW_BOOKMARK = "bookmark-bookmark-display";
	public static String COMMAND_PREVIOUS = "command-previous-na";
	// add
	public static String REQUEST_RESET = "request-reset";
	public static String REQUEST_RESET_USERID = "request-reset-userid";
	public static String GREETING = "greeting";
	public static String INPUT_USERID = "input-userid";
	public static String CONFIRM_TRUE = "confirm-true";
	public static String CONFIRM_FALSE = "confirm-false";
	public static String INPUT_CODE = "input-code";
	public static String SEND_CODE = "send-code";
	public static String SEND_CODE_EMAIL = "send-code-email";
	public static String SEND_CODE_SMS = "send-code-sms";
	public static String SEND_CODE_PHONECALL = "send-code-phonecall";
	public static String CHATTING = "chatting";
	// add
	// public static String MUSICAL = "music,musical";
	public static String STARTED_GENERICORDER = "<GenericOrder>";
	public static String ENDED_GENERICORDER = "</GenericOrder>";// <GenericOrder>
	// public static String Genre = "Genre";
	// public static String MusicGenre = "MusicGenre";
	// public static String SportGenre = "SportGenre";
	public static String Show = "show";
	public static String Play = "play";

	public static String SPORTS = "sports";
	public static String TV = "tv";
	public static String MOVIES = "movies";
	public static String END_OFF_SENTENCES_PREDICTION = ".\t.\t.\t.\t.\t.\t";

	// public static final String CALL_SIGN_MARKER = "|=c=|";

	// public static final String NORMALIZED_MARKER = "|=n=|";

	// public static final String SHORT_NAME_MARKER = "|=s=|";

	// public static final String AKA_MARKER = "|=a=|";

	// THRESHOLD TO DETERMINE EXACT_MATCH_FLAG
	public static final int CHARACTER_COUNT_THRESHOLD = 4;

	// ---------------------------------------VARIABLES FOR TIME
	// FEATURE--------------------------
	public static String TIME = "TIME";
	public static String DATE = "DATE";
	public static String YEAR = "YEAR";

	// ---------------------------------------VARIABLES FOR DETECTING
	// NUMBER-----------------------
	public static String ORDINAL = "ORDINAL";
	public static String NUMBER = "NUMBER";

	// ---------------------------------------VARIABLES FOR EXTRACTING
	// LABEL----------------------

	public static String[] CHECKED_BEFORE_YEAR = { "made in", "produced in", "created in", "generated in", "shot in",
			"filmed in", "released in", "made", "produced", "created", "generated", "shot", "filmed", "released" };

	private static String[] CHECKED_EPISODE_SEASON = { "last", "new", "old", "latest", "recent", "later" };
	public static Map<String, Integer> CHECKED_EPISODE_SEASON_MAP = new HashMap<String, Integer>();
	private static Pattern pattern;

	private static String REGEX = "\\<(.+?)\\>(.+?)\\</(.+?)\\>";
	private static String[] CHECKED_TERM = { "-", "â€“", "--", "'s", "?", "–", ".", "'re", "'ll", "n't", "'m",
			"'l", "\'", "/", "$", "&" };
	private static Map<String, Integer> CHECKED_TERM_MAP = new HashMap<String, Integer>();
	private static String[] IGNORED_STATION = { "station", "stations", "channel", "network", "networks" };
	private static String[] STOP_WORDS_ARRAY = { "the", "of", "a", "and", "in", "de", "to", "la", "no", "for", "on",
			"with" };

	// added 08/05/2014.
	private static String[] COMMAND_SEARCH_VALUES = { "find", "find all", "find some", "find the", "find me", "find us",
			"find me some", "go all", "get", "get any", "get me", "show", "show me", "show me the", "show me all the",
			"show me something", "how", "what", "what about", "what's", "what is", "what's happening",
			"what is happening", "any", "where", "click", "can", "which", "search", "search for", "is", "looking",
			"looking for a", "looking for", "looking for some", "anything with", "anything", "are any", "are",
			"are there any", "bring up", "buy me", "want to watch a", "watch some", "play me", "watch the", "watch a",
			"watch some", "showing", "display", "dose", "do", "find a", "find all", "find any", "find some", "find the",
			"finding", "give me", "give me some", "give me the", "give me see", "go get", "google", "guide",
			"guide for", "have", "showings", "browse", "browse me", "how about", "how about a", "see", "see a",
			"what's on", "is there any", "is there another", "is there a", "is there an", "is there", "only what",
			"only show", "only what's", "only what's on", "show us", "look up", "show us something",
			"find us something", "only", "raise", "search something", "show me any", "find me any", "show us any",
			"find us any", "show me listings", "find me listings", "show me some", "find me some", "show only",
			"find only", "there any", "what are some", "what are the", "what channel are", "what channel is",
			"what is the", "what channels", "what programs are", "what time", "what time is", "what time are",
			"what time do", "what time does", "what's on the", "what's playing on", "what's playing on the",
			"what's the", "when is", "when are", "fetch me", "fetch us", "get all", "get us", "get me", "how are the",
			"how is the", "what's up", "how is", "how are", "offer", "what's new", "when", "show about", "find about",
			"write about", "write", "turn me", "turn", "what is on", "what are on", "to watch", "turn to",
			"find me all the", "find all the", "show me all the", "show all the", "i'm looking", "look for",
			"what is on", "want", "want to watch", "want to know", "want to record", "want to see", "want to hear",
			"want to love", "love", "like", "prefer", "want to play", "tune to", "open", "bring me", "move to",
			"get me to", "whats" };
	private static String[] COMMAND_BUY_VALUES = { "buy", "purchase", "pay", "pay for", "order" };
	private static String[] COMMAND_PLAY_VALUES = { "play", "play me", "show", "show me", "pick", "select", "choose",
			"run", "view", "perform", "go", "go to", "display all", "start", "turn to", "open", "switch to", "take me",
			"take me to", "connect me", "connect me to", "change to", "change", "want", "want to watch", "want to know",
			"want to record", "want to see", "want to hear", "want to love", "love", "like", "prefer", "want to play",
			"watch" };
	private static String[] COMMAND_RECORD_VALUES = { "record", "copy", "write", "save", "file", "archive", "log",
			"capture" };

	private static String[] THREE_D_VALUES = { "3d", "3-d", "three-d", "3 d", "three d" };

	// added 03/27/2015 for processing special command intent.
	private static String[] SPECIAL_INTENT_VALUES = { "go to", "view", "change to", "change", "want", "like", "love",
			"prefer", "see", "take me", "watch", "connect me", "switch to", "tune to", "turn to", "play", "bring me",
			"move to", "get me to" };

	// added 04/01/2015 for Voice control single commands, no mention types
	// support start over intent
	public static String[] START_OVER_INTENT_VALUES = { "start over" };
	// support book mark intent
	public static String[] BOOKMARK_INTENT_VALUES = { "bookmark", "bookmarks" };
	// support book mark bookmark intent
	public static String[] BOOKMARK_BOOKMARK_INTENT_VALUES = { "add bookmark", "add bookmarks" };
	// support show book mark intent
	public static String[] SHOW_BOOKMARK_INTENT_VALUES = { "show bookmark", "show bookmarks" };
	// support back intent
	public static String[] PREVIOUS_INTENT_VALUES = { "back" };
	// support help intent
	public static String[] HELP_INTENT_VALUES = { "help" };
	// support info intent
	public static String[] INFO_INTENT_VALUES = { "more info", "more information" };
	// support reset intent
	public static String[] INFORM_INTENT_VALUE = { "reset", "try", "can't", "lost" };
	// support confirm true intent
	public static String[] CONFIRM_TRUE_INTENT_VALUE = { "yes", "ok", "reset", "correct" };
	// support confirm false intent
	public static String[] CONFIRM_FALSE_INTENT_VALUE = { "no", "don't", "incorrect" };
	// support greeting intent
	public static String[] GREETING_INTENT_VALUE = { "name" };
	// support account intent
	public static String[] USERID_INTENT_VALUE = { "userid", "account" };
	// support chating intent
	public static String[] CHATING_INTENT_VALUE = { "hello", "hi" };
	// support send email intent
	public static String[] EMAIL_INTENT_VALUE = { "email" };
	// support send email intent
	public static String[] SMS_INTENT_VALUE = { "sms" };
	// support send email intent
	public static String[] PHONECALL_INTENT_VALUE = { "phonecall" };

	public static Map<String, Integer> COMMAND_SEARCH_MAP = new HashMap<String, Integer>();
	public static Map<String, Integer> COMMAND_BUY_MAP = new HashMap<String, Integer>();
	public static Map<String, Integer> COMMAND_PLAY_MAP = new HashMap<String, Integer>();
	public static Map<String, Integer> COMMAND_RECORD_MAP = new HashMap<String, Integer>();

	public static Map<String, Integer> STOP_WORDS_MAP = new HashMap<String, Integer>();

	public static Map<String, Integer> IGNORED_STATION_MAP = new HashMap<String, Integer>();

	public static Map<String, Integer> SPECIAL_INTENT_MAP = new HashMap<String, Integer>();

	static {
		pattern = Pattern.compile(REGEX);
		// added check episode
		addValueToMap(CHECKED_EPISODE_SEASON, CHECKED_EPISODE_SEASON_MAP, "");
		// added CHECKED_TERM to hashMap
		addValueToMap(CHECKED_TERM, CHECKED_TERM_MAP, "");
		// added STOP_WORD to hashMap
		addValueToMap(STOP_WORDS_ARRAY, STOP_WORDS_MAP, "");
		// added IGNORED_STATION
		addValueToMap(IGNORED_STATION, IGNORED_STATION_MAP, "");
		// added command search value
		addValueToMap(COMMAND_SEARCH_VALUES, COMMAND_SEARCH_MAP, "");
		// added command buy value
		addValueToMap(COMMAND_BUY_VALUES, COMMAND_BUY_MAP, "");
		// added command play
		addValueToMap(COMMAND_PLAY_VALUES, COMMAND_PLAY_MAP, "");
		// added command record
		addValueToMap(COMMAND_RECORD_VALUES, COMMAND_RECORD_MAP, "");
		// added special command value
		addValueToMap(SPECIAL_INTENT_VALUES, SPECIAL_INTENT_MAP, "");
	}

	// -------------------------------------------FUNCTIONS-----------------------------------------

	private static void addValueToMap(String[] values, Map<String, Integer> maps, String space) {
		for (int i = 0; i < values.length; i++) {
			String value = space + values[i] + space;
			maps.put(value, i);
		}
	}

	/**
	 * This purpose of this function is to check the command value is special
	 * command or not.
	 * 
	 * @param intentKeywords
	 * @param searchedValue
	 * @return + true if it existed + false if it doesn't exist
	 * 
	 */
	public static boolean hasIntentValue(String[] intentValues, String searchedValue) {

		int valueIndex = Arrays.binarySearch(intentValues, searchedValue);

		if (valueIndex >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * This function to normalize
	 *
	 * @param inputString
	 * @return String
	 */
	public static String normalizeString(String inputString) {
		StringBuffer savedString = new StringBuffer();
		if (inputString == null || inputString.length() == 0) {
			return null;
		}
		String[] tokenString = inputString.trim().split(" "); // token string
		List<Integer> positionList = new ArrayList<Integer>();
		int length = tokenString.length;
		boolean isAtZero = true;

		// check if position of word is 's,-,'re,'ll, to save positionList
		for (int i = 0; i < length; i++) {
			if (CHECKED_TERM_MAP.containsKey(tokenString[i].trim())) {
				positionList.add(i);
			}
		}

		// check if positionList doesn't contain position
		if (positionList == null || positionList.size() == 0) {

			return inputString;
		}

		// positionList contains position
		// get subString first
		for (int i = 0; i < positionList.get(0) - 1; i++) {
			savedString.append(tokenString[i] + " ");
		}

		if (positionList.get(0) == 0) {
			if (tokenString[positionList.get(0)].equals("--")) {
				tokenString[positionList.get(0)] = "";
			}

			savedString.append(tokenString[positionList.get(0)]);
			isAtZero = false;
		} else {
			savedString.append(tokenString[positionList.get(0) - 1]);
		}

		int sizeWordOfPosition;
		// get the middle of String
		for (int i = 0; i < positionList.size() - 1; i++) {
			sizeWordOfPosition = tokenString[positionList.get(i)].length();
			if (positionList.get(i) > 0) {
				savedString.append(tokenString[positionList.get(i)]);
			}
			// check if word at position is 's
			if (sizeWordOfPosition > 1) {
				savedString.append(" ");
			}

			// append string follow range
			savedString.append(getRangeOfValues(tokenString, positionList.get(i) + 1, positionList.get(i + 1)));
			isAtZero = true;
		}

		// get the end of String

		int lastPosition = positionList.get(positionList.size() - 1);

		if (!isAtZero) {
			lastPosition++;
		}
		// check if lastPosition is equal (length - 1) then it's the end of
		// inputString
		if (lastPosition == (length - 1)) {
			if (tokenString[lastPosition].equals("--")) {
				tokenString[lastPosition] = "";
			}

			savedString.append(tokenString[lastPosition]);
		} else if (lastPosition < (length - 1)) {
			if (tokenString[lastPosition].equals("--")) {
				tokenString[lastPosition] = "-";
			}
			sizeWordOfPosition = tokenString[lastPosition].length();
			savedString.append(tokenString[lastPosition]);

			// check if sizeWordOfPosition is bigger than 1 then it's not -
			// character
			if (sizeWordOfPosition > 1) {
				savedString.append(" ");
			}

			savedString.append(getRangeOfValues(tokenString, lastPosition + 1, length));
		}

		return savedString.toString();
	}

	// get content follow a range of values
	private static String getRangeOfValues(String[] inputArray, int start, int end) {
		StringBuffer savedString = new StringBuffer();

		for (int i = start; i < end - 1; i++) {
			savedString.append(inputArray[i] + " ");
		}

		if (end - 1 > 0)
			savedString.append(inputArray[end - 1]);

		return savedString.toString();
	}

	/***
	 *
	 * @param arrays
	 * @return
	 */
	// public static boolean checkArrayNull(Integer[] arrays) {
	// for (int i = 0; i < arrays.length; i++) {
	// if (arrays[i] != null) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	public static boolean checkNotNull(String checkedValue) {
		if (checkedValue != null && checkedValue.length() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * add value not null and not duplicate
	 */
	public static void addNoNullNoDups(List<String> list, String newString) {
		if (newString == null) {
			return;
		}

		if (list.contains(newString)) {
			return;
		}

		list.add(newString);
	}

	/**
	 * @param inputString
	 *            Remove space, tab, newline in sentences
	 */
	public static String removeSpace(String inputString) {

		return inputString.trim().replaceAll("\\s+", " ");
	}

	public static List<String> splitPharse(String inputString, String delim) {
		List<String> words = new ArrayList<String>();

		// check if inputString is null or length of inputString is 0
		if (inputString == null || inputString.length() == 0) {
			return words;
		}

		StringTokenizer tokenizer = new StringTokenizer(inputString, delim);
		while (tokenizer.hasMoreTokens()) {
			words.add(tokenizer.nextToken());
		}

		return words;
	}

	// public static String removeDiacritics(String inputString) {
	// // remove space
	// inputString = inputString.trim().replaceAll("\\s+", " ");
	//
	// return Normalizer.normalize(inputString, Normalizer.Form.NFD)
	// .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	// }

	/**
	 *
	 * @param fileName
	 * @return regular expression in Perl 5 Regex format
	 */
	public static String loadPattern(String fileName) {
		BufferedReader in = null;
		StringBuffer line = new StringBuffer();
		String s;

		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			while ((s = in.readLine()) != null) {
				line.append(s);
			}
		} catch (Exception e) {
			LOGGER.error("Error: ", e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				LOGGER.error("Error: ", e);
			}
		}

		return line.toString();
	}

	/**
	 * This function is to check "3d" is contains in the Title value or not.
	 *
	 * @param titleValues
	 * @return true if it contains false if it doesn't contains
	 */
	public static boolean findThreeD(List<String> titleValues) {
		boolean isThreeD = false;

		if (titleValues == null) {
			return isThreeD;
		}

		int titleSize = titleValues.size();
		int threeDSize = THREE_D_VALUES.length;

		for (int i = 0; i < titleSize; i++) {
			String searchString = " " + titleValues.get(i) + " ";
			for (int j = 0; j < threeDSize; j++) {
				if (StringUtils.contains(searchString, " " + THREE_D_VALUES[j] + " ")) {
					isThreeD = true;
					break;
				}
			}
		}

		return isThreeD;
	}

	/**
	 * The purpose of this function is to remove "3d" in the title value.
	 * 
	 * @param titles
	 * @param isThreeD
	 * @return
	 */
	public static List<String> removeThreeDInTitle(List<String> titles, boolean isThreeD) {
		List<String> cloneTitles = titles;

		if (!isThreeD || cloneTitles == null || cloneTitles.size() <= 0) {
			return cloneTitles;
		}

		for (int i = 0; i < cloneTitles.size(); i++) {
			String searchString = " " + cloneTitles.get(i) + " ";
			for (int j = 0; j < THREE_D_VALUES.length; j++) {
				if (StringUtils.contains(searchString, " " + THREE_D_VALUES[j] + " ")) {
					cloneTitles.set(i, StringUtils.remove(searchString, THREE_D_VALUES[j] + " "));
				}
			}
		}

		return cloneTitles;
	}

	// private static String loadPattern(URL fileURL) {
	// BufferedReader in = null;
	// StringBuffer line = new StringBuffer();
	// String s;
	//
	// try {
	// in = new BufferedReader(new InputStreamReader(new FileInputStream(
	// new File(fileURL.toURI()))));
	// while ((s = in.readLine()) != null) {
	// line.append(s);
	// }
	// } catch (Exception e) {
	// LOGGER.error("Error: ", e);
	// } finally {
	// try {
	// in.close();
	// } catch (Exception e) {
	// LOGGER.error("Error: ", e);
	// }
	// }
	//
	// return line.toString();
	// }

	// private static String loadPattern(InputStream inputStream) {
	// BufferedReader in = null;
	// StringBuffer line = new StringBuffer();
	// String s;
	//
	// try {
	// in = new BufferedReader(new InputStreamReader(inputStream));
	// while ((s = in.readLine()) != null) {
	// line.append(s);
	// }
	// } catch (Exception e) {
	// LOGGER.error("Error: ", e);
	// } finally {
	// try {
	// in.close();
	// } catch (Exception e) {
	// LOGGER.error("Error: ", e);
	// }
	// }
	//
	// return line.toString();
	// }

	/** Returns true if object o == some element of array arrays. */
	// public static boolean contains(short[] arrays, int i) {
	// for (int item : arrays) {
	// if (item == i)
	// return true;
	// }
	// return false;
	// }

	// public static String replaceCharAt(String s, int start, int end, String
	// c) {
	// StringBuilder str = new StringBuilder(s).replace(start, end, c);
	// return str.toString();
	// }

	// public static int lengthOfString(List<String> words, int indexOfWord) {
	// StringBuffer savedString = new StringBuffer();
	// for (int i = 0; i < indexOfWord; i++) {
	// if (i >= 1) {
	// savedString.append(" ");
	// }
	// savedString.append(words.get(i));
	// }
	//
	// if (savedString.length() > 0) {
	// return savedString.length() + 1;
	// }
	//
	// return -1;
	// }

	// public static double countRateMatching(String value, List<Feature>
	// labelFeatures, StringBuffer savedResultMaching,
	// TypeIndex typeIndex) {
	//
	// double indexMatching = 0;
	// double preIndexMatching = 0;
	// double penaltyMatching = 0;
	//
	// if (value == null || value.length() == 0) {
	// return indexMatching;
	// }
	//
	// if (labelFeatures == null || labelFeatures.size() == 0) {
	// return indexMatching;
	// }
	//
	// List<String> words = NLUAnalyzerUtils.splitPharse(value, " ");
	// int size = words.size();
	// int sizeOfLabelsFeature = labelFeatures.size();
	// int indexOfLabelFeatures = 0;
	// int positionOfFeature = 0;
	//
	// // check if value of tag has contained feature lookup
	// for (int i = 0; i < size; i++) {
	// String searchedValue = words.get(i);
	// while (indexOfLabelFeatures < sizeOfLabelsFeature) {
	//
	// if
	// (labelFeatures.get(indexOfLabelFeatures).getOriginalObservation().equals(searchedValue))
	// {
	// boolean result = false;
	// if (typeIndex.getCode() == TypeIndex.title.getCode()) {
	// for (int idx = 0; idx < TypeIndex.title.getSize(); idx++) {
	// result =
	// labelFeatures.get(indexOfLabelFeatures).findTypeFeature(FeatureEnums.B_title.getCode()
	// + idx);
	// if (result) {
	// break;
	// }
	// }
	// } else {
	// result =
	// labelFeatures.get(indexOfLabelFeatures).findTypeFeature(typeIndex.getCode());
	// }
	// if (result) {
	// if (labelFeatures.get(indexOfLabelFeatures).getPositionOfFeature() ==
	// positionOfFeature) {
	// indexMatching++;
	//
	// if (i >= 1) {
	// savedResultMaching.append(" ");
	// }
	// savedResultMaching.append(searchedValue);
	//
	// if (indexMatching >= preIndexMatching) {
	// preIndexMatching = indexMatching;
	// if (positionOfFeature == 1) {
	// penaltyMatching++;
	// }
	// }
	//
	// positionOfFeature++;
	// } else {
	// positionOfFeature = 0;
	// if (penaltyMatching >=1) {
	// penaltyMatching--;
	// }
	//
	// if (indexMatching >= preIndexMatching) {
	// preIndexMatching = indexMatching;
	// }
	// if (labelFeatures.get(indexOfLabelFeatures).getPositionOfFeature() ==
	// positionOfFeature) {
	// indexMatching = 0;
	// i = i - 1;
	// indexOfLabelFeatures--;
	// }
	// }
	// }
	//
	// indexOfLabelFeatures++;
	// break;
	// }
	// indexOfLabelFeatures++;
	// }
	// }
	//
	// if (penaltyMatching >= 1) {
	// preIndexMatching += penaltyMatching;
	// }
	//
	// return (double) (preIndexMatching / size);
	// }
	//
	// private static String checkNormalizer(String checkedValue,
	// MentionType mentionType, NluDataBean nluDataBean) {
	// String savedResult = nluDataBean.getNormalizeService().normalize(
	// mentionType, checkedValue);
	//
	// return savedResult;
	// }

	/**
	 * NluIntentDTO merging
	 */

	// public static String getString(String cur, String last) {
	// if (cur != null) {
	// return cur;
	// }
	//
	// return last;
	// }

	// public static List<LiteralTag> getStringList(List<LiteralTag>
	// currentList,
	// List<LiteralTag> lastList) {
	// if (currentList != null && currentList.size() > 0) {
	// return currentList;
	// }
	//
	// return lastList;
	// }

	// public static List<CanonicalDTO> getListCanonicalDTO(
	// List<CanonicalDTO> cur, List<CanonicalDTO> last) {
	// if (cur != null) {
	// return cur;
	// }
	//
	// return last;
	//
	// }

	// public static List<CanonicalStationDTO> getListCanonicalStationDTO(
	// List<CanonicalStationDTO> cur, List<CanonicalStationDTO> last) {
	// if (cur != null) {
	// return cur;
	// }
	//
	// return last;
	//
	// }

	public static boolean checkingKeywordSearch(String literal, LabelTags[] tags) {
		boolean isKeywordSearch = false;

		if (literal == null || literal.length() == 0 || tags == null || tags.length == 0) {
			return isKeywordSearch;
		}

		Matcher matcher = pattern.matcher(literal);
		while (matcher.find()) {
			String startedLabel = matcher.group(1);
			boolean hit = false;
			for (LabelTags tag : tags) {
				if (tag.name().equals(startedLabel)) {
					hit = true;
				}
			}
			if (hit) {
				isKeywordSearch = true;
			} else {
				isKeywordSearch = false;
				break;
			}
		}

		return isKeywordSearch;
	}

	// public static boolean checkingKeywordSearch(String literal) {
	// boolean isKeywordSearch = false;
	//
	// if (literal == null || literal.length() == 0) {
	// return isKeywordSearch;
	// }
	//
	// Matcher matcher = pattern.matcher(literal);
	// while (matcher.find()) {
	// String startedLabel = matcher.group(1);
	// if (LabelTags.Other.name().equals(startedLabel)
	// || LabelTags.Description.name().equals(startedLabel)) {
	// isKeywordSearch = true;
	// } else {
	// isKeywordSearch = false;
	// break;
	// }
	// }
	//
	// return isKeywordSearch;
	// }

	public static String actorNormalize(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String encodeValue(String value) {
		try {
			LOGGER.info("Start encoding " + value);
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("UTF-8 not supported, " + e);
		}

		return value;
	}
}
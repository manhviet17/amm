/*
 * MentionType.java
 *
 * Copyright (c) 2012 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.dto;

/**
 * The Enum MentionType.
 */
public enum MentionType {

	UNKNOWN(0, 0, true, new String[] {}), MEDIA_TYPE(11, 100, false,
			new String[] { "MovieInfo", "TvInfo" }), GENRE(12, 50, true,
			new String[] { "Genre" }), CREDIT(13, 10, true, new String[] {
			"Actor", "Director" }), TITLE(14, 10, true,
			new String[] { "Title" }),USERID(13, 10, true, new String[] {
					"UserId",}),NAME(13, 10, true, new String[] {
							"Name",}), DESCRIPTION(15, 10, false,
			new String[] { "Description" }), TIME(16, 10, false,
			new String[] { "Time" }), SOURCE(18, 10, false,
			new String[] { "source" }), CHANNEL(19, 10, false,
			new String[] { "Channel" }), STATIONS(23, 10, true,
			new String[] { "Station" }), QUALITY_RATING(28, 10, false,
			new String[] { "QualityRating" }), QUALITY_RATING_COMP(29, 10,
			false, new String[] {}), CONTENT_RATING(30, 10, false,
			new String[] { "ContentRating" }), CONTENT_RATING_COMP(31, 10,
			false, new String[] {}), SPORT_TEAM(36, 10, false,
			new String[] { "SportTeam" }), SPORT_LEAGUE(37, 10, false,
			new String[] { "SportLeague" }), SPORT_EVENT(38, 10, false,
			new String[] { "SportEvent" }), SPORT_GENRE(39, 10, false,
			new String[] {}), ROVI_MOOD(41, 10, false, new String[] {}), ROVI_TONE(
			41, 10, false, new String[] {}), INTENT(43, 10, false,
			new String[] {}), EPISODE(44, 10, false, new String[] {}), SEASON(
			45, 10, false, new String[] {}), MUSIC_GENRE(45, 10, true,
			new String[] { "MusicGenre" }),  ROLE(48, 10, true,
					new String[] { "Role" }), CODE(13, 10, true, new String[] {"Code",}),
			CONFIRM(13, 10, true, new String[] {"Confirm",});

	/** The code. */
	private int code = 0;

	private int weight = 0;

	private boolean collection = false;

	/** The other names */
	private String[] otherNames = new String[] {};

	/**
	 * Instantiates a new mention type.
	 *
	 * @param code
	 *            The code
	 * @param messageKey
	 *            The message key
	 */
	private MentionType(int code, int weight, boolean collection,
			String[] otherNames) {
		this.code = code;
		this.weight = weight;
		this.collection = collection;
		this.otherNames = otherNames;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Gets other names.
	 *
	 * @return the other names
	 */
	public String[] getOtherNames() {
		return otherNames;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @return the collection
	 */
	public boolean isCollection() {
		return collection;
	}

	/**
	 * Value.
	 *
	 * @return the string
	 */
	public String value() {
		return name();
	}

	/**
	 * From value.
	 *
	 * @param v
	 *            the v
	 * @return the state type
	 */
	public static MentionType fromValue(String v) {
		return valueOf(v);
	}

	public static MentionType fromOtherName(String otherName) {
		if (otherName == null || "".equals(otherName)) {
			return MentionType.UNKNOWN;
		}

		for (MentionType type : MentionType.values()) {
			for (String name : type.getOtherNames()) {
				if (name.equalsIgnoreCase(otherName)) {
					return type;
				}
			}
		}

		return MentionType.UNKNOWN;
	}

	public static MentionType fromName(String name) {
		if (name == null || "".equals(name)) {
			return MentionType.UNKNOWN;
		}

		for (MentionType type : MentionType.values()) {
			if (name.equalsIgnoreCase(type.name())) {
				return type;
			}
		}

		return MentionType.UNKNOWN;
	}

}
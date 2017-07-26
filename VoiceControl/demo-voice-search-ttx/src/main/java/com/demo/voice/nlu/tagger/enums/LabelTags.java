/*
 * LabelTags.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.tagger.enums;

public enum LabelTags {
	UNKNOWN(0), // filler
	Credit(1), // --
	ChannelNumber(2), // Channel
	Command(3), // Intent
	CommandBuy(4), // IntentBuy
	CommandPlay(5), // IntentPlay
	CommandRecommend(6), // IntentRecommend
	CommandRecord(7), // IntentRecord
	CommandSearch(8), // IntentSearch
	Date(9), // date
	Delivery(10), // source
	Description(11), // --
	Episode(12), // TvShowEpisode
	GenericOrder(13), // --
	Genre(14), // --
	Language(15), // --
	League(16), // SportLeague
	Mood(17), // filler
	Movie(18), // MovieInfo
	Other(19), // filler
	ParentalRating(20), // ContentRating
	QualityDescription(21), // filler
	Role(22), // Description
	RoviTone(23), // Description
	Season(24), // TvShowSeason
	SportEvent(25), // --
	SportGenre(26), // --
	SportRelated(27), // SportsInfoToDiscard
	StarRating(28), // QualityRating
	Station(29), // --
	Team(30), // SportTeam
	Time(31), // --
	Title(32), // --
	Tv(33), // TvInfo
	Year(34), // --
	MusicGenre(35),

	//--------- Kien add
	AccountNumber(36),
	// Command already exist
	ConfirmFalse(37),
	ConfirmTrue(38),
	// Date already exist
	Name(40),
	// Other already exist
	PersonID(41),
	Confirm(42),
	Code(42),
	UserId(43);
	
	/** The code. */
	private int code = 0;

	LabelTags(int code) {
		this.code = code;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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
	public static LabelTags fromValue(String v) {
		return valueOf(v);
	}

	public static LabelTags fromName(String name) {
		if (name == null || "".equals(name)) {
			return LabelTags.UNKNOWN;
		}

		for (LabelTags type : LabelTags.values()) {
			if (name.equalsIgnoreCase(type.name())) {
				return type;
			}
		}

		return LabelTags.UNKNOWN;
	}
}

/*
 * NluIntentDTO.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.analyzer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.demo.voice.nlu.tagger.dto.LiteralTag;

/**
 *
 * The Class NluIntentDTO.java.
 *
 * @author Dung Tran
 * @author Vinh Nguyen
 *
 */
public class NluIntentDTO implements Serializable {

	private static final long serialVersionUID = -7301000810281798876L;

	protected String intent;
	protected String text;
	protected String literal;
	protected String mediaType;
	protected String genre;
	protected String userId;
	protected String name;
	protected String code;
	protected String delivery;
	protected String request;

	protected List<CanonicalDTO> titles;
	protected List<CanonicalDTO> credits;

	protected transient List<LiteralTag> taggedLiteral = new ArrayList<LiteralTag>();

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CanonicalDTO> getTitles() {
		return titles;
	}

	public void setTitles(List<CanonicalDTO> titles) {
		this.titles = titles;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return "NluIntentDTO [intent=" + intent + ", text=" + text + ", value=" + literal + ", mediaType=" + mediaType
				+ ", genre=" + genre + ", titles=" + titles + ", credits=" + credits;
	}

	public List<CanonicalDTO> getCredits() {
		return credits;
	}

	public void setCredits(List<CanonicalDTO> credits) {
		this.credits = credits;
	}
}

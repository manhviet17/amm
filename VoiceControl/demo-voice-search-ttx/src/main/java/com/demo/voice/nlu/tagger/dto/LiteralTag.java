/*
 * LiteralTag.java
 *
 * Copyright (c) 2014 DIRECTV, Inc.
 * An Unpublished Work.  All Rights Reserved.
 *
 * DIRECTV PROPRIETARY:  The information contained in or disclosed by this
 * document is considered proprietary by DIRECTV, Inc.  This document and/or the
 * information contained therein shall not be duplicated nor disclosed in whole
 * or in part without the specific written permission of DIRECTV, Inc.
 */
package com.demo.voice.nlu.tagger.dto;

import java.io.Serializable;

/**
 * 
 * The Class LiteralTag.java
 *
 * @author Dung Tran
 *
 */
public class LiteralTag implements Serializable {

	private static final long serialVersionUID = 1253933220260344800L;
	private String taggedName;
	private String value;
	
	public String getTaggedName() {
		return taggedName;
	}
	public void setTaggedName(String taggedName) {
		this.taggedName = taggedName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

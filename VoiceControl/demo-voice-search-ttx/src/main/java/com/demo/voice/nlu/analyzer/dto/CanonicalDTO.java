/*
 * CanonicalDTO.java
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

import com.google.common.base.Objects;

/**
 * 
 * The Class CanonicalDTO.java.
 *
 * @author Dung Tran
 *
 */
public class CanonicalDTO implements Serializable {

	private static final long serialVersionUID = 7260734933565395868L;

	protected List<CanonicalElementDTO> canonicals;

	protected String value;
	
	protected boolean exactMatchFlag;

	public List<CanonicalElementDTO> getCanonicals() {
		if (canonicals == null || canonicals.size() == 0) {
			return new ArrayList<CanonicalElementDTO>();
		}
		return canonicals;
	}

	public void setCanonicals(List<CanonicalElementDTO> canonicals) {
		this.canonicals = canonicals;
	}
	
	public String getText() {
		
		if (canonicals != null && canonicals.size() > 0 && canonicals.get(0) != null && canonicals.get(0).getName() != null
				&&!"".equals(canonicals.get(0).getName())) {
			return canonicals.get(0).getName();
		}
		
		if (value == null) {
			return "";
		}
		
		return value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof CanonicalDTO) {
			CanonicalDTO that = (CanonicalDTO) object;
			return Objects.equal(this.value, that.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects
				.hashCode(value);
	}

	@Override
	public String toString() {
		if (canonicals == null || canonicals.size() == 0) {
			return Objects.toStringHelper(this)
			.add("value", value)
			.toString();
		} else {
			return Objects.toStringHelper(this).add("value", value).add("canonicals", (canonicals)).toString();
		}
	}

	public boolean isExactMatchFlag() {
		return exactMatchFlag;
	}

	public void setExactMatchFlag(boolean exactMatchFlag) {
		this.exactMatchFlag = exactMatchFlag;
	}
}
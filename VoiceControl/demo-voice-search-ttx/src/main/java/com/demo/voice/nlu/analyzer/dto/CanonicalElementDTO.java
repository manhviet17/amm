/*
 * CanonicalElementDTO.java
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

import com.google.common.base.Objects;

/**
 * 
 * The Class CanonicalElementDTO.java.
 *
 * @author Dung Tran
 *
 */
public class CanonicalElementDTO implements Serializable {
	
	private static final long serialVersionUID = -7808872139941951499L;

	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof CanonicalElementDTO) {
			CanonicalElementDTO canonical = (CanonicalElementDTO) arg0;
			return this.name.equals(canonical.getName());
		}
		return super.equals(arg0);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", name).toString();
	}
}

package com.demo.voice.process.cache;

import java.math.BigInteger;

public class AccountInfo {
	private String personId;
	private String name;
	private String date;
	private String accountId;
	private BigInteger accountBalance;
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public BigInteger getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigInteger accountBalance) {
		this.accountBalance = accountBalance;
	}
}

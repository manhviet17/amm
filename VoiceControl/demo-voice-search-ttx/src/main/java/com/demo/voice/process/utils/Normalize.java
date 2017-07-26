package com.demo.voice.process.utils;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import com.demo.voice.process.cache.Cache;
import com.demo.voice.process.model.OtherData;

public class Normalize {
	private Cache cache;
	private OtherData other;
	
	public String convertToDateFormat(String value) {
		return value.replace(" " + other.getMonth() + " ", "/").replace(" " + other.getYear() + " ", "/");
	}
	
	public String normalizeText(String text){
		// Remove the spaces between numbers only
		text = normallizeNumber(text);
		return text.replaceAll("(?<=\\d) +(?=\\d)", ""); 
	}
	
	// Normalize text -> number : mot -> 1, hai -> 2,...
	public String normallizeNumber(String str) {
		// If str is date -> do not normalize
		if (str.contains(other.getMonth())) {
			// If only have month in text -> add year 
			if (!str.contains(other.getYear())) {
				str = addYear(str);
			}
			return str;
		}
		Set<String> keys = cache.getNormalizeNumberData().keySet();
		for (String key : keys) {
			String replace = cache.getNormalizeNumberData().get(key);
			str = str.toLowerCase().replace(key, replace);
		}
		return str;
	}
	
	// Normalize amount
	public String normallizeAmount(String str) {
		Set<String> keys = cache.getNormalizeAmountData().keySet();
		for (String key : keys) {
			String replace = cache.getNormalizeAmountData().get(key);
			str = str.toLowerCase().replace(key, replace);
		}
		return str;
	}
	
	private String addYear(String str) {
		String[] strs = str.split(" ");
		StringBuilder strB = new StringBuilder();
		
		for (int i = 0; i < strs.length ; i++) {
			if (other.getMonth().equalsIgnoreCase(strs[i])) {
				strB.append(strs[i] + " ");
				strB.append(strs[i+1] + " ");
				strB.append(other.getYear() + " ");
				i = i+2;
			}
			strB.append(strs[i] + " ");
		}
		
		return strB.toString().trim();
	}

	/**
	 * normalize text -> number
	 * @param str
	 * @return
	 */
	public BigInteger calculateAmount(String str) {
		//Remove noise
		str = str.replaceAll("[.,]", "");
		
		// Normalize text -> number
		str = normallizeNumber(str);
		str = normallizeAmount(str);
		
		// Remove space between 2 digit only
		// str = str.replaceAll("(?<=\\d) +(?=\\d)", "");
		
		// Split
		String[] strs = str.split(" ");
		
		BigInteger amount = new BigInteger("0");
		Map<String, String> units = cache.getUnitData();
		for (int i = 0; i < strs.length; i++) {
			// If word is number
			if (strs[i].matches("[0-9]+")) {
				if (i < strs.length -1 && units.get(strs[i+1]) != null) {
					amount = amount.add(new BigInteger(strs[i] + units.get(strs[i+1])));
				} else {
					amount = amount.add(new BigInteger(strs[i]));
				}
			}
			// If word is text
			// TODO:
		}
		return amount;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	public void buildOtherData(){
		other = cache.getOtherData();
	}
}

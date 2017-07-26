package com.demo.voice.process.cache;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.demo.voice.process.model.OtherData;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CacheLoader {
	
	public static List<OtherData> getOthers(String otherPath) {
		List<OtherData> lst = new ArrayList<OtherData>();
        File directory = new File(otherPath);
        File [] files = directory.listFiles();
        ObjectMapper mapper = new ObjectMapper();
        OtherData other = null;
       
        for (int i = 0; i < files.length ; i++) {
               try {
            	   other = mapper.readValue(new File(files[i].getAbsolutePath()), OtherData.class);
                   lst.add(other);
               } catch (Exception e) {
                   e.printStackTrace();
               }
        }
       
        return lst;
	}
	
	public static Map<String, AccountInfo> getInfoData(String otherPath) {
		Map<String, AccountInfo> map = new HashMap<String, AccountInfo>();
        File directory = new File(otherPath);
        File [] files = directory.listFiles();
        ObjectMapper mapper = new ObjectMapper();
        AccountInfo accountInfo = null;
       
        for (int i = 0; i < files.length ; i++) {
               try {
            	   accountInfo = mapper.readValue(new File(files[i].getAbsolutePath()), AccountInfo.class);
            	   map.put(accountInfo.getPersonId(), accountInfo);
               } catch (Exception e) {
                   e.printStackTrace();
               }
        }
       
        return map;
	}
	
	public static Map<String, String> getMapDataFromFile(
			String file) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = in.readLine()) != null) {
				String parts[] = line.split(" : ");
				map.put(parts[0], parts[1]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}

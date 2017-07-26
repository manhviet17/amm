package com.demo.voice.process.cache;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataGenerator {
	public static final int PERSON_ID_INDEX = 0;
	public static final int NAME_INDEX = 1;
	public static final int DATE_INDEX = 2;
	public static final int ACCOUNT_ID_INDEX = 3;
	public static final int ACCOUNT_BALANCE_INDEX = 4;
	
	private static final String DATA_FILE = "D:\\FPT\\NLU\\Vietnamese\\LST_141116_Banking account.xlsx";
	private static final String SHEET = "data";
	
	private static final String OUTPUT_PATH = "D:\\FPT\\NLU\\Vietnamese\\data\\raw_data\\accountInfo\\";
	
	public static void main(String[] args) {
		DataGenerator dataGenerator = new DataGenerator(); 
		dataGenerator.generateData();
		System.out.println("Done!!!");
	}
	
	public void generateData(){
		List<AccountInfo> contents = getAccountInfoDataFromExcel();
		
		generate(contents, OUTPUT_PATH);
	}
	
	private void generate(List<AccountInfo> AccountInfos, String path) {
		for (AccountInfo accountInfo : AccountInfos) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(new File(path + accountInfo.getPersonId() + ".json"), accountInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<AccountInfo> getAccountInfoDataFromExcel(){
		List<AccountInfo> lst = new ArrayList<AccountInfo>();
		
		try {
			FileInputStream file = new FileInputStream(new File(DATA_FILE));
			// Open Excel file, notice that excel file must be close first
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// Open Sheet 
			XSSFSheet sheet = workbook.getSheet(SHEET);

			int numberOfRow = sheet.getLastRowNum();
			
			AccountInfo accountInfo;
			Row row;
			
			for (int i = 1; i <= numberOfRow ; i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				accountInfo = new AccountInfo();
				
				// Person_ID
				if (row.getCell(PERSON_ID_INDEX) != null) {
					if (row.getCell(PERSON_ID_INDEX).getCellType() == Cell.CELL_TYPE_STRING) {
						accountInfo.setPersonId(row.getCell(PERSON_ID_INDEX).getStringCellValue());
					}
				}
				// Name
				if (row.getCell(NAME_INDEX) != null) {
					if (row.getCell(NAME_INDEX).getCellType() == Cell.CELL_TYPE_STRING) {
						accountInfo.setName(row.getCell(NAME_INDEX).getStringCellValue().trim().toLowerCase());
					} 
				}
				
				// Date
				if (row.getCell(DATE_INDEX) != null) {
					if (row.getCell(DATE_INDEX).getCellType() == Cell.CELL_TYPE_STRING) {
						accountInfo.setDate(row.getCell(DATE_INDEX).getStringCellValue().trim().toLowerCase());
					} 
				}
				
				// AccountId
				if (row.getCell(ACCOUNT_ID_INDEX) != null) {
					if (row.getCell(ACCOUNT_ID_INDEX).getCellType() == Cell.CELL_TYPE_STRING) {
						accountInfo.setAccountId(row.getCell(ACCOUNT_ID_INDEX).getStringCellValue());
					} 
				}
				
				// Account Balance
				if (row.getCell(ACCOUNT_BALANCE_INDEX) != null) {
					if (row.getCell(ACCOUNT_BALANCE_INDEX).getCellType() == Cell.CELL_TYPE_STRING) {
						accountInfo.setAccountBalance(new BigInteger(row.getCell(ACCOUNT_BALANCE_INDEX).getStringCellValue()));
					}
				}
				
				if (accountInfo.getPersonId() != null) {
					lst.add(accountInfo);
				}
			}
            
            // Close file stream
            file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lst;
	}
}

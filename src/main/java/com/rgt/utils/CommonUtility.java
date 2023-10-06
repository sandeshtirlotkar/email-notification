package com.rgt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.rgt.entity.RegisterUserEntity;

public class CommonUtility {
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"ddMMyyyy");
	
	public static Date getDateFromString(String dateObj){
		SimpleDateFormat dateFormat = new SimpleDateFormat( "DD/MM/YYYY hh:mm");

	    Date parsedTimeStamp = null;
		try {
			parsedTimeStamp = dateFormat.parse(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	    return parsedTimeStamp;
	}
	
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	//value from properties
	public static final String getValueFromPropeties(String key, String fileName) {
		Properties properties;
		String keyValue = null;
		try {
			properties = PropertiesLoaderUtils
					.loadProperties(new ClassPathResource(fileName + Constant.PROPERTIES_FILE_TYPE));
			keyValue = properties.getProperty(key);
		} catch (IOException e) {
			e.getStackTrace();
		}
		return keyValue;
	}
	
	//read tpl file method
	public static String readTPLfile(String fileName) {
		InputStream fileStream = null;
		String dataRead = null;
		try {
			fileStream = new ClassPathResource(fileName, CommonUtility.class.getClassLoader()).getInputStream();
			//CommonUtility.class.getResourceAsStream(fileName);
			
		//	fileStream = CommonUtility.class.getResourceAsStream(fileName);
			byte[] b = new byte[fileStream.available()];
			fileStream.read(b);

			fileStream.close();
			dataRead = new String(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataRead;
	}
	
	public static void replace(StringBuilder builder, String from, String to) {
		int index = builder.indexOf(from);
		if (index != -1) {
			builder.replace(index, index + from.length(), to);
		}
	}
	
	public static String getCurrentDateDDMMYYYY(Date createdOn) {
		String date = dateFormatter.format(createdOn);
		return date;
	}
	
	
	

}

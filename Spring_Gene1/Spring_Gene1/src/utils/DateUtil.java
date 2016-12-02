package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateUtil {
	
	 private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
	        @Override
	        protected DateFormat initialValue() {
	            return new SimpleDateFormat("yyyyMMddhhmmssSSS");
	        }
	    };

	    public static Date parse(String dateStr) throws ParseException {
	        return threadLocal.get().parse(dateStr);
	    }

	    public static String format(Date date) {
	        return threadLocal.get().format(date);
	    }

}

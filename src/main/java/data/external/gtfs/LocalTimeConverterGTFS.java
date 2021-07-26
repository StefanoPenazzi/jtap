package data.external.gtfs;

import java.time.LocalTime;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class LocalTimeConverterGTFS extends AbstractBeanField {
    @Override
    protected LocalTime convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
    	Integer hh = Integer.parseInt(s.substring(0, 2));
    	if(hh<=23) {
    		LocalTime lt = LocalTime.parse(s);
	        return lt;
    	}
    	else {
    		hh-=24;
    		String hhs = hh.toString();
    		if(hhs.length() == 1) {
    			hhs = "0"+hhs;
    		}
    		LocalTime lt = LocalTime.parse(hhs+s.substring(2,8));
	        return lt;
    	}
    }
}
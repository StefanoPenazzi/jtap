package data.utils.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.protobuf.Method;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;



public class CSV {
	
public static <T> List<T> getList(File fileName, Class<T> type, Integer skipLines) throws IOException {
		
		List<T> res = new CsvToBeanBuilder<T>(new FileReader(fileName))
				.withSkipLines(skipLines)
                .withType(type)
                .build()
                .parse();
        
        return res;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> void writeTo(File fileName,List<T> inputList) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Class<T> type;
		try (FileWriter writer = new FileWriter(fileName)) {
			HeaderColumnNameMappingStrategy<T> mappingStrategy =  new HeaderColumnNameMappingStrategy<T>();
		    mappingStrategy.setType((Class<? extends T>) inputList.get(0).getClass());
		    StatefulBeanToCsv<T> beanWriter = new StatefulBeanToCsvBuilder<T>(writer)
		        .withMappingStrategy(mappingStrategy)
		        .withApplyQuotesToAll(false)
		        .build();
		    beanWriter.write(inputList);
		}
	}
	
	public static void writeTo(File fileName,String s) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

		try (FileWriter writer = new FileWriter(fileName)) {
		    writer.append(s);
		    writer.flush();
		    writer.close();
		}
	}

}

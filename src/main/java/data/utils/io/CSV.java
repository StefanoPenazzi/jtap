package data.utils.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.Method;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
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
    
    public static <T> List<T> getListByHeaders(File fileName, Class<T> type) throws IOException {
		
    	HeaderColumnNameMappingStrategy<T> ms = new HeaderColumnNameMappingStrategy<T>();
        ms.setType(type);
    	
		List<T> res = new CsvToBeanBuilder<T>(new FileReader(fileName))
                .withType(type)
                .withMappingStrategy(ms)
                .build()
                .parse();
        
        return res;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> void writeTo(File fileName,List<T> inputList) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Class<T> type;
		try (FileWriter writer = new FileWriter(fileName)) {
		    StatefulBeanToCsv<T> beanWriter = new StatefulBeanToCsvBuilder<T>(writer)
		        .withMappingStrategy(new AnnotationStrategy(inputList.iterator().next().getClass()))
		        .withApplyQuotesToAll(false)
		        .build();
		    beanWriter.write(inputList);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void writeToDefault(File fileName,List<T> inputList) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Class<T> type;
		try (FileWriter writer = new FileWriter(fileName)) {
		    StatefulBeanToCsv<T> beanWriter = new StatefulBeanToCsvBuilder<T>(writer)
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
	
//	private static class AnnotationStrategy extends HeaderColumnNameTranslateMappingStrategy
//    {
//        public AnnotationStrategy(Class<?> clazz)
//        {
//            Map<String,String> map=new HashMap<>();
//            for(Field field:clazz.getDeclaredFields())
//            {
//                CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
//                if(annotation!=null)
//                {
//                    map.put(annotation.column(),annotation.column());
//                }
//            }
//            setType(clazz);
//            setColumnMapping(map);
//        }
//
//        @Override
//        public String[] generateHeader(Object bean) throws CsvRequiredFieldEmptyException
//        {
//            String[] result=super.generateHeader(bean);
//            for(int i=0;i<result.length;i++)
//            {
//                result[i]=getColumnName(i);
//            }
//            return result;
//        }
//    }
	
	private static class AnnotationStrategy<T> extends HeaderColumnNameTranslateMappingStrategy<T> {
	    Map<String, String> columnMap = new HashMap<>();
	    public AnnotationStrategy(Class<? extends T> clazz) {

	        for (Field field : clazz.getDeclaredFields()) {
	            CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
	            if (annotation != null) {

	                    columnMap.put(field.getName().toUpperCase(), annotation.column());
	            }
	        }
	        setType(clazz);      
	    }

	    @Override
	    public String getColumnName(int col) {
	        String name = headerIndex.getByPosition(col);
	        return name;
	    }

	    public String getColumnName1(int col) {
	        String name = headerIndex.getByPosition(col);
	        if(name != null) {
	            name = columnMap.get(name);
	        }
	        return name;
	    }
	    @Override
	    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
	        String[] result = super.generateHeader(bean);
	        for (int i = 0; i < result.length; i++) {
	            result[i] = getColumnName1(i);
	        }
	        return result;
	    }
	}

}

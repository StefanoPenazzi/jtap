package data.external.neo4j;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import com.opencsv.bean.CsvBindByName;

import config.Config;
import controller.Controller;
import core.graph.LinkI;
import core.graph.NodeGeoI;
import core.graph.NodeI;
import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;
import core.graph.rail.RailLink;
import data.utils.io.CSV;
import data.utils.io.OS;

/**
 * @author stefanopenazzi
 *
 */
public class Utils {
	
	private static String CSVLINE = "csvLine";
	
	/**
	 * @param query
	 * @param am
	 * @return
	 * @throws Exception
	 */
	public static List<Record> runQuery(String query, AccessMode am ) throws Exception {
		List<Record> res = null;
		String database = Controller.getConfig().getNeo4JConfig().getDatabase();
		try( Neo4jConnection conn = Controller.getInjector().getInstance(Neo4jConnection.class)){  
			res = conn.query(database,query,am);
		}
		return res;
	}
	

	/**
	 * @param database
	 * @param query
	 * @param am
	 * @return
	 * @throws Exception
	 */
	public static List<Record> runQuery(String database,String query, AccessMode am ) throws Exception {
		List<Record> res = null;
		try( Neo4jConnection conn = new Neo4jConnection()){  
			res = conn.query(database,query,am);
		}
		return res;
	}
	
	/**
	 * @param database
	 * @param query
	 * @param am
	 * @return
	 * @throws Exception
	 */
	public static List<Record> runQuery( Neo4jConnection conn,String database, String query, AccessMode am ) throws Exception {
		List<Record> res = null;
		res = conn.query(database,query,am);
		return res;
	}
	
	/**
	 * @param file
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public static String getLoadCSVNodeQuery(String file, Class<? extends NodeI> element) throws Exception {
		if(!element.isAnnotationPresent(Neo4JNodeElement.class)) {
			throw new Exception(element.getName() +" is not annotated with Neo4JNodeElement");
		}
		return "LOAD CSV WITH HEADERS FROM \"file:///"+file+"\" AS "+CSVLINE +" "+ getCSVNodeString(element);
	}
	
	/**
	 * @param element
	 * @return
	 */
	public static String getCSVNodeString(Class<? extends NodeI> element) {
		
		Map<String, Field> elementFields = Stream.of(element.getDeclaredFields())
	              .filter(field -> field.isAnnotationPresent(Neo4JPropertyElement.class) 
	            		  && field.isAnnotationPresent(CsvBindByName.class))
	              .collect(Collectors.toMap(field -> field.getAnnotation(Neo4JPropertyElement.class).key(),
	            		  Function.identity()));
		StringBuffer res = new StringBuffer();
		res.append("CREATE (p");
		for(String s: element.getAnnotation(Neo4JNodeElement.class).labels()) {
			res.append(":"+s);
		}
		//Properties
		res.append("{");
		Iterator it = elementFields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        res.append(pair.getKey()+":"+((Field)pair.getValue()).getAnnotation(Neo4JPropertyElement.class).type()+
	        		"("+CSVLINE+"."+((Field)pair.getValue()).getAnnotation(CsvBindByName.class).column()+")");
	        if(it.hasNext()) {
	        	res.append(",");
	        }
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		res.append("})");
		return res.toString();
	}
	
	/**
	 * @param file
	 * @param linkElement
	 * @param sfNodeElement
	 * @param sfProperty
	 * @param sfCsvProperty
	 * @param stNodeElement
	 * @param stProperty
	 * @param stCsvProperty
	 * @return
	 * @throws Exception
	 */
	public static  String getLoadCSVLinkQuery(String file, Class<? extends LinkI> linkElement, Class<? extends NodeI> sfNodeElement, String sfProperty, String sfCsvProperty,
			Class<? extends NodeI> stNodeElement, String stProperty, String stCsvProperty) throws Exception {
		
		
		if(!linkElement.isAnnotationPresent(Neo4JLinkElement.class)) {
			throw new Exception(linkElement.getName() +" is not annotated with Neo4JLinkElement");
		}
		if(!sfNodeElement.isAnnotationPresent(Neo4JNodeElement.class) || 
				!stNodeElement.isAnnotationPresent(Neo4JNodeElement.class)) {
			throw new Exception(sfNodeElement.getName() +"or/and"+ stNodeElement.getName()  +" is/are not annotated with Neo4JNodeElement");
		}else {
			Boolean annotationFieldsfNodeElement = FieldUtils.getFieldsListWithAnnotation(sfNodeElement, Neo4JPropertyElement.class).stream()
					.filter(f -> f.getAnnotation(Neo4JPropertyElement.class)
							.key().equals(sfProperty)).toArray().length == 1? true:false;
			Boolean annotationFieldstNodeElement = FieldUtils.getFieldsListWithAnnotation(stNodeElement, Neo4JPropertyElement.class).stream()
					.filter(f -> f.getAnnotation(Neo4JPropertyElement.class)
							.key().equals(stProperty)).toArray().length == 1? true:false;
			if(!(annotationFieldsfNodeElement && annotationFieldstNodeElement)) {
				throw new Exception("Property not found");
			}
		}
		
		Field sfFiled = FieldUtils.getFieldsListWithAnnotation(sfNodeElement, Neo4JPropertyElement.class).stream().filter(v -> v.getAnnotation(Neo4JPropertyElement.class)
					.key().equals(sfProperty)).findFirst().orElse(null);
		Field stFiled = FieldUtils.getFieldsListWithAnnotation(stNodeElement, Neo4JPropertyElement.class).stream().filter(v -> v.getAnnotation(Neo4JPropertyElement.class)
				.key().equals(stProperty)).findFirst().orElse(null);
		
		String res = "USING PERIODIC COMMIT 1000 LOAD CSV WITH HEADERS FROM \"file:///"+file+"\" AS "+CSVLINE +
				" MATCH (sf:"+sfNodeElement.getAnnotation(Neo4JNodeElement.class).labels()[0]+" {"+sfProperty+":"+sfFiled.getAnnotation(Neo4JPropertyElement.class).type()+"(" +
		CSVLINE+"."+sfCsvProperty+")}),(st:"+
		stNodeElement.getAnnotation(Neo4JNodeElement.class).labels()[0]+" {"+stProperty+":"+stFiled.getAnnotation(Neo4JPropertyElement.class).type()+"(" +CSVLINE+"."+stCsvProperty+")}) "+
		getCSVLinkString(linkElement,"sf","st");
		
		return res;
	}
    
    /**
     * @param element
     * @param sf
     * @param st
     * @return
     */
    public static String getCSVLinkString(Class<? extends LinkI> element, String sf, String st) {
		
		Map<String, Field> elementFields = Stream.of(element.getDeclaredFields())
	              .filter(field -> field.isAnnotationPresent(Neo4JPropertyElement.class) 
	            		  && field.isAnnotationPresent(CsvBindByName.class))
	              .collect(Collectors.toMap(field -> field.getAnnotation(Neo4JPropertyElement.class).key(),
	            		  Function.identity()));
		StringBuffer res = new StringBuffer();
		res.append("CREATE ("+sf+")-[:"+element.getAnnotation(Neo4JLinkElement.class).label()+" {");
		//Properties
		Iterator it = elementFields.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        res.append(pair.getKey()+":"+((Field)pair.getValue()).getAnnotation(Neo4JPropertyElement.class).type()+
	        		"("+CSVLINE+"."+((Field)pair.getValue()).getAnnotation(CsvBindByName.class).column()+")");
	        if(it.hasNext()) {
	        	res.append(",");
	        }
	    }
		res.append("}]->("+st+")");
		return res.toString();
	}

    /**
     * @param <T>
     * @param database
     * @param tempDirectory
     * @param nodes
     * @throws Exception
     */
    public static <T extends NodeI> void insertNodes(String database,String tempDirectory,List<T> nodes) throws Exception {
    	String fileName = null;
    	if(nodes.size()==0) return;
		if(nodes.get(0).neo4JNode()) {
			fileName = nodes.get(0).getLabels()[0] +"_file.csv";
		}
		else {
			throw new Exception("Only classes annotated with @Neo4JNodeElement can be converted in nodes");
		}
		CSV.writeTo(new File(tempDirectory+"/"+fileName),nodes);
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,data.external.neo4j.Utils.getLoadCSVNodeQuery(tempDirectory+"/"+fileName,nodes.get(0).getClass()),AccessMode.WRITE );
	    }
		//OS.delete(new File(tempDirectory+"/"+fileName));
    }
    
    /**
     * @param <T>
     * @param database
     * @param tempDirectory
     * @param links
     * @param linkElement
     * @param sfNodeElement
     * @param sfProperty
     * @param sfCsvProperty
     * @param stNodeElement
     * @param stProperty
     * @param stCsvProperty
     * @throws Exception
     */
    public static <T extends LinkI> void insertLinks(String database,String tempDirectory,List<T> links,Class<? extends LinkI> linkElement, Class<? extends NodeI> sfNodeElement, String sfProperty, String sfCsvProperty,
			Class<? extends NodeI> stNodeElement, String stProperty, String stCsvProperty) throws Exception {
    	String fileName = null;
    	if(links.size()==0) return;
		if(links.get(0).getClass().isAnnotationPresent(Neo4JLinkElement.class)) {
			fileName = links.get(0).getClass().getAnnotation(Neo4JLinkElement.class).label() +"_file.csv";
		}
		else {
			throw new Exception("Only classes annotated with @Neo4JLinkElement can be converted in links");
		}
		CSV.writeTo(new File(tempDirectory+"/"+fileName),links);
		try( Neo4jConnection conn = new Neo4jConnection()){  
			conn.query(database,data.external.neo4j.Utils.getLoadCSVLinkQuery(tempDirectory+"/"+
		fileName,linkElement,sfNodeElement,sfProperty,sfCsvProperty,stNodeElement,stProperty,stCsvProperty),AccessMode.WRITE );
	    }
		OS.delete(new File(tempDirectory+"/"+fileName));
    }
    
    
    public static <T extends LinkI> void insertLinks(List<T> links,Class<? extends LinkI> linkElement, Class<? extends NodeI> sfNodeElement, String sfProperty, String sfCsvProperty,
			Class<? extends NodeI> stNodeElement, String stProperty, String stCsvProperty) throws Exception {
    	String database = Controller.getConfig().getNeo4JConfig().getDatabase();
    	String tempDirectory = Controller.getConfig().getGeneralConfig().getTempDirectory();
    	insertLinks(database,tempDirectory,links,linkElement,sfNodeElement,sfProperty,sfCsvProperty,stNodeElement,stProperty,stCsvProperty);
    }
    
    public static <T extends LinkI> void insertLinks(List<T> links,Class<? extends LinkI> linkElement, Class<? extends NodeI> sfNodeElement, String sfProperty,
			Class<? extends NodeI> stNodeElement, String stProperty) throws Exception {
    	String database = Controller.getConfig().getNeo4JConfig().getDatabase();
    	String tempDirectory = Controller.getConfig().getGeneralConfig().getTempDirectory();
    	String sfCsvProperty = sfProperty;
    	String stCsvProperty = stProperty;
    	insertLinks(database,tempDirectory,links,linkElement,sfNodeElement,sfProperty,sfCsvProperty,stNodeElement,stProperty,stCsvProperty);
    }
    
    
    public static <T extends NodeI> List<T> importNodes(Neo4jConnection conn,String database,Class<T> nodeClass) throws Exception{
    	List<T> result = new ArrayList<>(); 
    	Constructor<T> nodeConstructor = nodeClass.getConstructor();
    	T node =  nodeConstructor.newInstance(); 
    	String query = "match (n:"+node.getLabels()[0]+") return n";
	    List<Record> records = runQuery(conn,database,query,AccessMode.READ);
	    for(Record rec: records) {
	    	result.add(core.graph.Utils.map2GraphElement(rec.values().get(0).asMap(),nodeClass));
	    }
	    return result;
    }
    
    public static <T extends NodeI> List<T> importNodes(String database,Class<T> nodeClass) throws Exception{
    	List<T> result = new ArrayList<>(); 
    	Constructor<T> nodeConstructor = nodeClass.getConstructor();
    	T node =  nodeConstructor.newInstance(); 
    	String query = "match (n:"+node.getLabels()[0]+") return n";
    	List<Record> records = null;
    	try( Neo4jConnection conn = new Neo4jConnection()){  
    		records = runQuery(conn,database,query,AccessMode.READ);
    	}
	    for(Record rec: records) {
	    	result.add(core.graph.Utils.map2GraphElement(rec.values().get(0).asMap(),nodeClass));
	    }
	    return result;
    }
    
    public static <T extends NodeI> List<T> importNodes(Class<T> nodeClass) throws Exception{
    	
    	List<T> result = new ArrayList<>(); 
    	Constructor<T> nodeConstructor = nodeClass.getConstructor();
    	T node =  nodeConstructor.newInstance(); 
    	String query = "match (n:"+node.getLabels()[0]+") return n";
    	List<Record> records = null;
    	records = runQuery(query,AccessMode.READ);
	    for(Record rec: records) {
	    	result.add(core.graph.Utils.map2GraphElement(rec.values().get(0).asMap(),nodeClass));
	    }
	    return result;
    }
    
    
    /**
	 * @param conn
	 * @param database
	 * @param graphName
	 * @param nodesQuery
	 * @param linksQuery
	 * @throws Exception
	 */
	public static void createGraphCatalog(Neo4jConnection conn,String database,String graphName,String nodesQuery, String linksQuery) throws Exception {
		conn.query(database,
				"CALL gds.graph.create.cypher('"+graphName+"',"
						+ "'"+nodesQuery+"',"
								+ "'"+linksQuery+"')",AccessMode.WRITE );
	}
	
	/**
	 * @param conn
	 * @param database
	 * @param graphName
	 * @throws Exception
	 */
	public static void deleteGraphCatalog(Neo4jConnection conn,String database, String graphName) throws Exception { 
        conn.query(database,"CALL gds.graph.drop('"+graphName+"') YIELD graphName;",AccessMode.WRITE );
	}
	
	/**
	 * @param conn
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public static List<Record> getCatalog(Neo4jConnection conn,String database) throws Exception{
		List<Record> res = null;
		res= conn.query(database," CALL gds.graph.list()"
			 		+ " YIELD graphName,database, nodeCount, relationshipCount, schema;",AccessMode.READ);
		return res;
	}
	
	/**
	 * @param database
	 * @param label
	 * @param property
	 * @throws Exception
	 */
	public static void createIndex(String database, String indexName ,String label, String property) throws Exception {
		try( Neo4jConnection conn = new Neo4jConnection()){
			String q = "CREATE INDEX "+indexName+" IF NOT EXISTS FOR (n:"+label+") ON (n."+property+")";
			conn.query(database,q,AccessMode.WRITE);
		}
	}
	
	/**
	 * @param conn
	 * @param database
	 * @param label
	 * @param property
	 * @throws Exception
	 */
	public static void createIndex(Neo4jConnection conn,String indexName, String database, String label, String property) throws Exception {
		String q = "CREATE INDEX "+indexName+" IF NOT EXISTS FOR (n:"+label+") ON (n."+property+")";
	}

    /**
     * @param database
     * @param label
     * @param property
     * @throws Exception 
     */
    public static void deleteIndex(String database, String label, String property) throws Exception {
    	try( Neo4jConnection conn = new Neo4jConnection()){
			String q = "DROP INDEX ON:"+label+"("+property+")";
			conn.query(database,q,AccessMode.WRITE);
		}
	}
    
    /**
     * @param conn
     * @param database
     * @param label
     * @param property
     */
    public static void deleteIndex(Neo4jConnection conn, String database, String label, String property) {
		String q = "DROP INDEX ON:"+label+"("+property+")";
		conn.query(database,q,AccessMode.WRITE);
	}
    
    /**
     * @param database
     * @param indexName
     */
    public static void deleteIndex(String database, String indexName) throws Exception {
    	try( Neo4jConnection conn = new Neo4jConnection()){
			String q = "DROP INDEX "+indexName+" IF EXISTS";
			conn.query(database,q,AccessMode.WRITE);
		}
	}
    
    /**
     * @param conn
     * @param database
     * @param label
     * @param property
     */
    public static void deleteIndex(Neo4jConnection conn, String database, String indexName) {
		String q = "DROP INDEX "+indexName+" IF EXISTS";
		conn.query(database,q,AccessMode.WRITE);
	}
}

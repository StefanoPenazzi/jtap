package core.graph;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import core.graph.annotations.GraphElementAnnotation;
import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElement;

/**
 * @author stefanopenazzi
 *
 */
public interface GraphElement {
	 
	 /**
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	default Map<String,Object> getProperties() throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = this.getClass().getFields();
	    Map<String, Object> map = new HashMap<String, Object>();
	    for(Field f : fields) {
	    	if(f.isAnnotationPresent(Neo4JPropertyElement.class)) {
	    		 map.put(f.getAnnotation(Neo4JPropertyElement.class).key(),f.get(this));
	    	}
	    }
	    return map;
     }
	 
	 /**
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	default Object getPropertyValue(String s) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = this.getClass().getFields();
	    for(Field f : fields) {
	    	if(f.isAnnotationPresent(Neo4JPropertyElement.class)) {
	    		if(f.getAnnotation(Neo4JPropertyElement.class).key().equals(s)) {
	    			return f.get(this);
	    		}
	    	}
	    }
	    return null;
	 }
	
	
	 /**
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	default GraphElementAnnotation.Neo4JType getPropertyNeo4JType(String s) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = this.getClass().getFields();
	    for(Field f : fields) {
	    	if(f.isAnnotationPresent(Neo4JPropertyElement.class)) {
	    		if(f.getAnnotation(Neo4JPropertyElement.class).key().equals(s)) {
	    			return f.getAnnotation(Neo4JPropertyElement.class).type();
	    		}
	    	}
	    }
	    return null;
	 }
}

package core.graph.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface GraphElementAnnotation {
	
	enum Neo4JType{
		
		TOFLOAT("toFloat"),
		TOINTEGER("toInteger"),
		TOBOOLEAN("toBoolean"),
		TOSTRING("toString"),
		;
		
		String parser = null;
		Neo4JType(final String parser) {
	        this.parser = parser;
	    }

	    public String getValue() {
	        return this.parser;
	    }
	}
	
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Neo4JNodeElement {
		public String[] labels();
	}
	
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Neo4JLinkElement {
		public String label();
	}
	
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Neo4JPropertyElement {
		public String key();
		public Neo4JType type();
	}
	
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Neo4JPropertyElementMethod {
		public String key();
		public Neo4JType type();
	}

}

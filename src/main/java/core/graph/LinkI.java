package core.graph;

import core.graph.annotations.GraphElementAnnotation.Neo4JLinkElement;

/**
 * @author stefanopenazzi
 *
 */
public interface LinkI extends GraphElement {
	
	/**
	 * @return
	 * @throws Exception
	 */
	default String getLabel() throws Exception {
		String res = null;
		if(neo4JLink()) {
			res = this.getClass().getAnnotation(Neo4JLinkElement.class).label();
		}
		else {
			throw new Exception("Only classes annotated with @Neo4JLinkElement can be converted in links");
		}
		return res;
	}
	
	default Boolean neo4JLink() {
		if(this.getClass().isAnnotationPresent(Neo4JLinkElement.class)) {
			return true;
		}
		else {
			return false;
		}
	}
}

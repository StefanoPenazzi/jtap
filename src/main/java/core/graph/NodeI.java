package core.graph;

import core.graph.annotations.GraphElementAnnotation.Neo4JNodeElement;

/**
 * @author stefanopenazzi
 *
 */
public interface NodeI extends GraphElement {
	
	/**
	 * @return
	 * @throws Exception
	 */
	default String[] getLabels() throws Exception {
		String[] res = null;
		if(neo4JNode()) {
			res = this.getClass().getAnnotation(Neo4JNodeElement.class).labels();
		}
		else {
			throw new Exception("Only classes annotated with @Neo4JNodeElement can be converted in nodes");
		}
		return res;
	}
	
	default Boolean neo4JNode() {
		if(this.getClass().isAnnotationPresent(Neo4JNodeElement.class)) {
			return true;
		}
		else {
			return false;
		}
	}

}

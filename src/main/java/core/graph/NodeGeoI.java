package core.graph;

import core.graph.annotations.GraphElementAnnotation.Neo4JPropertyElementMethod;
import core.graph.annotations.GraphElementAnnotation.Neo4JType;

public interface NodeGeoI extends NodeI {
	
	@Neo4JPropertyElementMethod(key="lat",type=Neo4JType.TOFLOAT) 
	public Double getLat();
	
	@Neo4JPropertyElementMethod(key="lon",type=Neo4JType.TOFLOAT) 
	public Double getLon();
	
	public Long getId();
}
